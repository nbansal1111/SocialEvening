package com.project.socialevening.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.socialevening.ListAdapters.CustomListAdapter;
import com.project.socialevening.ListAdapters.CustomListAdapterInterface;
import com.project.socialevening.R;
import com.project.socialevening.models.ContactsQuery;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.Logger;
import com.project.socialevening.utility.Preferences;
import com.project.socialevening.utility.SMSUtil;
import com.project.socialevening.utility.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nitin on 30/10/15.
 */
public class AddTeamMateFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, CustomListAdapterInterface, AdapterView.OnItemClickListener {
    private ListView listView;
    private CustomListAdapter<PhoneContact> adapter;
    private List<PhoneContact> contacts = new ArrayList<>();
    private List<PhoneContact> selectedContacts = new ArrayList<>();
    private int selectedCount;
    private View headerview;
    private String teamObjectID;
    private ParseObject teamObject;
    private TextView headerText;

    public static AddTeamMateFragment getInstance(String teamObjectID) {
        AddTeamMateFragment f = new AddTeamMateFragment();
        f.teamObjectID = teamObjectID;
        return f;
    }

    public static AddTeamMateFragment getInstance(Bundle bundle) {
        AddTeamMateFragment f = new AddTeamMateFragment();
        f.setArguments(bundle);
        return f;
    }


    @Override
    public void initViews() {
        Util.saveAppLink();
        listView = (ListView) findView(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        setAdapter(contacts);
        getLoaderManager().initLoader(0, null, this);
        setOnClickListener(R.id.btn_skip, R.id.btn_send_invite);
        setFooter(false);
        loadTeamObject();
        showToast(getString(R.string.loading_contacts));
    }

    private void loadTeamObject() {
        Bundle b = getArguments();
        teamObjectID = b.getString(AppConstants.BUNDLE_KEYS.TEAM_ID);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(AppConstants.PARAMS.TEAM);
        query.whereEqualTo("objectId", teamObjectID);
        executeTask(AppConstants.TASK_CODES.PARSE_QUERY, query);

    }

    @Override
    public void onPreExecute(int taskCode) {
    }

    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        switch (taskCode) {
            case AppConstants.TASK_CODES.PARSE_QUERY:
                List<ParseObject> scoreList = (List<ParseObject>) response;
                if (scoreList != null && scoreList.size() > 0) {
                    setHeader(scoreList.get(0));
                }
                break;
        }
    }

    private void setHeader(ParseObject teamObject) {
        if (teamObject != null) {
            this.teamObject = teamObject;
            ParseFile file = teamObject.getParseFile(AppConstants.PARAMS.TEAM_IMAGE);
            if (null != file) {
                String url = file.getUrl();
                ImageView headerImage = (ImageView) headerview.findViewById(R.id.iv_team_image);
                Util.loadImage(getActivity(), url, headerImage, 0);
            }
            headerText = (TextView) headerview.findViewById(R.id.tv_team_name);
            headerText.setText(teamObject.getString(AppConstants.PARAMS.TEAM_NAME) + "");
        }
    }

    private void setAdapter(List<PhoneContact> contacts) {
        listView.removeAllViewsInLayout();
        if (headerview != null) {
            listView.removeHeaderView(headerview);
        } else {
            headerview = getHeaderView();
        }
        listView.addHeaderView(headerview);
        adapter = new CustomListAdapter<>(getActivity(), R.layout.row_contact, contacts, this);
        listView.setAdapter(adapter);
    }

    private View getHeaderView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.view_gradient, null);
    }

    @Override
    public int getViewID() {
        return R.layout.fragment_add_team_mate;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Logger.info(TAG, "onCreateLoader");
        // run on background thread
        return new CursorLoader(getActivity(), ContactsQuery.CONTENT_URI,
                ContactsQuery.PROJECTION, ContactsQuery.SELECTION, null,
                ContactsQuery.SORT_ORDER);
    }

    HashSet<String> contactIds = new HashSet<>();

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Will run on main thread
        if (null != cursor && cursor.getCount() > 0) {
            new ContactsTask().execute(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, int resourceID) {
        PhoneContact contact = contacts.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getActivity()).inflate(resourceID, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.contactName.setText(contact.contactName);
        holder.contactNumber.setText(contact.contactNumber);
        if (contact.isSelected) {
            holder.isSelected.setImageResource(R.drawable.ic_invitation_selected);
        } else {
            holder.isSelected.setImageResource(R.drawable.ic_invitation_unselected);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 0th position for header view
        if (position == 0) return;
        PhoneContact contact = contacts.get(position - 1);
        contact.isSelected = !contact.isSelected;
        if (contact.isSelected) {
            selectedContacts.add(contact);
        } else {
            selectedContacts.remove(contact);
        }
        setFooter(selectedContacts.size() > 0);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_skip:
                getActivity().finish();
                break;
            case R.id.btn_send_invite:
                Log.d(TAG, "Trying to send sms" + selectedContacts.size());
                SMSUtil util = new SMSUtil(getActivity());

                for (PhoneContact c : selectedContacts) {
                    util.sendSMS(c.contactNumber, getAppLink());
                }
                showToast(getString(R.string.msg_invitation_success));
                resetAdapter();
                break;
        }
    }

    private void resetAdapter() {
        for (PhoneContact c : selectedContacts) {
            contacts.remove(c);
        }
        adapter.notifyDataSetChanged();
        selectedContacts.clear();
        setFooter(selectedContacts.size() > 0);
    }

    private void setFooter(boolean isSelected) {
        Button skip = (Button) findView(R.id.btn_skip);
        Button sendInvite = (Button) findView(R.id.btn_send_invite);
        if (isSelected) {
            sendInvite.setVisibility(View.VISIBLE);
            skip.setBackgroundColor(getResources().getColor(R.color.color_skip_inv_bg));
            skip.setTextColor(getResources().getColor(R.color.color_purple_bg));
        } else {
            sendInvite.setVisibility(View.GONE);
            skip.setBackgroundColor(getResources().getColor(R.color.color_purple_bg));
            skip.setTextColor(getResources().getColor(R.color.white));
        }
    }


    private class ViewHolder {
        TextView contactName, contactNumber;
        ImageView isSelected;

        ViewHolder(View view) {
            contactName = (TextView) view.findViewById(R.id.tv_inviteeName);
            contactNumber = (TextView) view.findViewById(R.id.tv_inviteePhone);
            isSelected = (ImageView) view.findViewById(R.id.iv_invitation_checkbox);
        }
    }


    private class ContactsTask extends AsyncTask<Cursor, Void, List<PhoneContact>> {

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected List<PhoneContact> doInBackground(Cursor... params) {
            Cursor c = params[0];
            List<PhoneContact> contacts = fetchContacts(c);
            return contacts;
        }

        @Override
        protected void onPostExecute(List<PhoneContact> phoneContacts) {
            hideProgressBar();
            contacts = phoneContacts;
            setAdapter(contacts);

        }

        private List<PhoneContact> fetchContacts(Cursor cursor) {
            List<PhoneContact> contacts = new ArrayList<PhoneContact>();
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                PhoneContact contact = new PhoneContact();
                contact.contactName = (cursor.getString(ContactsQuery.DISPLAY_NAME));
                contact.contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                //
                //  Get all phone numbers.
                //
                ContentResolver cr = getActivity().getContentResolver();
                Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                if (null != phones && phones.moveToFirst()) {
                    contact.contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.add(contact);
                    phones.close();
                }
            }
            cursor.close();
            return contacts;
        }

    }


    private class PhoneContact {
        private String contactId;
        private String contactName;
        private String contactNumber;
        private boolean isSelected;
    }

    private String getAppLink() {
        StringBuilder builder = new StringBuilder();
        builder.append("Download app -- " + Preferences.getAppLink());
        return builder.toString();
    }


}
