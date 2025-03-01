package com.app.safedot_familysafetycare.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.safedot_familysafetycare.adapters.InviteAdapter
import com.app.safedot_familysafetycare.adapters.MemberAdapter
import com.app.safedot_familysafetycare.databases.SafeDotDB
import com.app.safedot_familysafetycare.databinding.FragmentHomeBinding
import com.app.safedot_familysafetycare.models.Contact
import com.app.safedot_familysafetycare.models.Member
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val contactsList: ArrayList<Contact> = ArrayList()

    private lateinit var inviteAdapter: InviteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For Members
        val listMembers = listOf<Member>(
            Member("Dharmin Tandel"),
            Member("Fenil Tandel"),
            Member("Mitali Patel"),
            Member("Pratik Patel"),
            Member("Kinjal Panchal"),
            Member("Parth Panchal"),
            Member("Jainil Mistry"),
            Member("Jay Mistry"),
            Member("Jinal Mistry"),
            Member("Jaimin Patel")
        )

        val memberAdapter = MemberAdapter(this.requireContext(), listMembers)
        binding.rvMembers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMembers.adapter = memberAdapter

        // For Invite Contacts

        inviteAdapter = InviteAdapter(this.requireContext(), contactsList)

        fetchDBContacts()

        CoroutineScope(Dispatchers.IO).launch {
            insertDBContacts(fetchContacts())
        }

        binding.rvInvites.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvInvites.adapter = inviteAdapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchDBContacts() {
        val db = SafeDotDB.getDatabase(requireContext())
        db.contactDao().getAllContacts().observe(viewLifecycleOwner) {
            contactsList.clear()
            contactsList.addAll(it)
            inviteAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun insertDBContacts(contactsList: ArrayList<Contact>) {
        val db = SafeDotDB.getDatabase(requireContext())
        db.contactDao().insertAll(contactsList)
    }

    @SuppressLint("Range")
    private fun fetchContacts(): ArrayList<Contact> {
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        val listContacts: ArrayList<Contact> = ArrayList()

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhoneNumber > 0) {
                    val pCursor =
                        cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            ""
                        )

                    if (pCursor != null && pCursor.count > 0) {
                        while (pCursor.moveToNext()) {
                            val phoneNum =
                                pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            listContacts.add(Contact(name, phoneNum))
                        }

                        pCursor.close()
                    }
                }
            }

            cursor.close()
        }
        return listContacts
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}