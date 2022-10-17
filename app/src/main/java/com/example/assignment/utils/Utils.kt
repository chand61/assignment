package com.example.assignment.utils

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.annotation.RequiresPermission
import com.example.assignment.model.ContactModel


@RequiresPermission(permission.READ_CONTACTS)
@SuppressLint("Range")
fun getContacts(ctx: Context): ArrayList<ContactModel>? {
    val list: ArrayList<ContactModel> = ArrayList(arrayListOf())
    val contentResolver: ContentResolver = ctx.getContentResolver()
    val cursor: Cursor? = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )

    if (cursor != null && cursor.count > 0) {
        while (cursor.moveToNext()) {
            val id: String = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val contact = retrieveContactDetails(id, ctx)
            if (contact != null) {
                list.add(contact)
            }
        }
        return list
    } else {
        return null
    }
}

@SuppressLint("Range")
@RequiresPermission(permission.READ_CONTACTS)
public fun retrieveContactDetails(
    id: String,
    context: Context,
): ContactModel? {
    var contact: ContactModel? = null
    val mobileNoSet = HashSet<Long>()
    context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf<String>(id), null
    )?.use {
        if (it.moveToFirst()) {
            do {

                val contactId = it.getLong(it.getColumnIndex(CONTACT_PROJECTION[0]))
                if (mobileNoSet.add(contactId)) {
                    val name = it.getString(it.getColumnIndex(CONTACT_PROJECTION[2])) ?: ""
                    val hasPhoneNumber =
                        it.getString(it.getColumnIndex(CONTACT_PROJECTION[3])).toInt()
                    val phoneNumber = if (hasPhoneNumber > 0) {
                        context.retrievePhoneNumber(contactId)
                    } else mutableListOf()
                    val email = context.retrieveEmail(contactId)
                    val websites = context.retrieveWebsite(contactId)
                    val avatar = context.retrieveAvatar(contactId)
                    val birthday = context.retrieveBirthday(contactId)
                    contact = ContactModel(
                        contactId.toString(),
                        name,
                        email,
                        phoneNumber,
                        websites,
                        avatar,
                        birthday
                    )
                }
            } while (it.moveToNext())
        }
    }
    return contact
}

@SuppressLint("Range")
private fun Context.retrievePhoneNumber(contactId: Long): MutableList<String> {

    val result: MutableList<String> = mutableListOf()
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} =?",
        arrayOf(contactId.toString()),
        null
    )?.use {
        if (it.moveToFirst()) {
            do {
                result.add(it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
            } while (it.moveToNext())
        }
    }
    return result

}

@SuppressLint("Range")
private fun Context.retrieveEmail(contactId: Long): MutableList<String> {
    val result: MutableList<String> = mutableListOf()
    contentResolver.query(
        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
        null,
        "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} =?", arrayOf(contactId.toString()),
        null
    )?.use {
        if (it.moveToFirst()) {
            do {
                result.add(it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)))
            } while (it.moveToNext())
        }
    }
    return result
}

@SuppressLint("Range")
private fun Context.retrieveWebsite(contactId: Long): MutableList<String> {

    val result: MutableList<String> = mutableListOf()

    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Website.URL,
        ContactsContract.CommonDataKinds.Website.TYPE
    )
    val selection: String =
        ContactsContract.Data.CONTACT_ID.toString() + " = " + contactId + " AND " + ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE + "'"

    contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        projection,
        selection,
        null,
        null
    )

        ?.use {
            if (it.moveToFirst()) {
                do {
                    result.add(it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL)))
                } while (it.moveToNext())
            }
        }

    return result
}

private fun Context.retrieveAvatar(contactId: Long): Uri? {
    return contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} =? AND ${ContactsContract.Data.MIMETYPE} = '${ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE}'",
        arrayOf(contactId.toString()),
        null
    )?.use {
        if (it.moveToFirst()) {
            val contactUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI,
                contactId
            )
            Uri.withAppendedPath(
                contactUri,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
            )
        } else null
    }
}

@SuppressLint("Range")
private fun Context.retrieveBirthday(contactId: Long): String {
    val birthday = "N/A"
    val columns = arrayOf(
        ContactsContract.CommonDataKinds.Event.START_DATE,
        ContactsContract.CommonDataKinds.Event.TYPE,
        ContactsContract.CommonDataKinds.Event.MIMETYPE
    )

    val where: String =
        ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and " + ContactsContract.Data.CONTACT_ID + " = " + contactId

    val selectionArgs: Array<String>? = null
    val sortOrder = ContactsContract.Contacts.DISPLAY_NAME

    val birthdayCur: Cursor? =
        contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            columns,
            where,
            selectionArgs,
            sortOrder
        )
    if (birthdayCur != null) {
        if (birthdayCur.count > 0) {
            while (birthdayCur.moveToNext()) {
                return birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
            }
        }
        birthdayCur.close()
    }
    return birthday
}

private val CONTACT_PROJECTION = arrayOf(
    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
    ContactsContract.Contacts.LOOKUP_KEY,
    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
    ContactsContract.Contacts.HAS_PHONE_NUMBER,
)