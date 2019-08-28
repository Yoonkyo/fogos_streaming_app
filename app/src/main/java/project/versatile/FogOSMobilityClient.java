package project.versatile;

import FlexID.FlexID;
import FogOSControl.Client.FogOSClient;
import FogOSMessage.QueryMessage;
import FogOSMessage.ReplyMessage;
import FogOSMessage.RequestMessage;
import FogOSMessage.ResponseMessage;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FogOSMobilityClient extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;
    public static final String KEY_FLEX_ID_DATA = "flex";
    private static final String TAG = "FogOS";

    private EditText editSearch;
    private ListView listView;
    private FogOSClient fogos;
    private ListAdapter listAdapter;
    private int count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fogos = new FogOSClient();

        // Generate the list with the search bar
        setContentView(R.layout.activity_main);
        editSearch = (EditText) findViewById(R.id.edittext);
        listView = (ListView) findViewById(R.id.listView);

        listAdapter = new ListAdapter();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestMessage requestMessage;
                ResponseMessage responseMessage;
                Intent intent = new Intent(getApplicationContext(), MobilityActivity.class);
                ListItem item = (ListItem) listAdapter.getItem(position);
                FlexID peer = item.getFlexID();
                Toast.makeText(getApplicationContext(), "선택: " + item.getTitle(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "After getting the peer's Flex ID");
                requestMessage = fogos.makeRequestMessage(peer);
                responseMessage = fogos.sendRequestMessage(requestMessage);
                peer = responseMessage.getPeerID();
                Log.d(TAG, "After requesting the connection with the peer's Flex ID");
                FlexIDParcel data = new FlexIDParcel(peer);
                Log.d(TAG, "After making the peer's Flex ID parcelable");
                intent.putExtra(KEY_FLEX_ID_DATA, data);
                Log.d(TAG, "After putting the extra data into the bundle");

                // TODO: Need to revise MobilityActivity to view the video
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });
    }

    public void onButton1Clicked(View v) {
        QueryMessage queryMessage;
        ReplyMessage replyMessage;
        String keywords;
        JSONArray idList;

        count = (count + 1) % 2;

        // Prepare the query message
        keywords = editSearch.getText().toString();
        queryMessage = fogos.makeQueryMessage(keywords);

        // Send the query message and get the reply message
        replyMessage = fogos.sendQueryMessage(queryMessage);

        // TODO: ID List should be more abstracted.
        idList = replyMessage.getIDList();

        // Add the list with the list of the replies including a flex ID, a description, and a title
        try {
            listAdapter.delAllItem();
            JSONObject obj;
            for (int i=0; i<idList.length(); i++) {
                obj = idList.getJSONObject(i);
                listAdapter.addItem(new ListItem(obj.getString("title"), obj.getString("desc"), obj.getString("id")));
            }
            listView.setAdapter(listAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ListAdapter extends BaseAdapter {
        ArrayList<ListItem> itemArray = new ArrayList<ListItem>();

        public void addItem(ListItem item) {
            itemArray.add(item);
        }

        public void delAllItem() {
            itemArray.clear();
        }

        @Override
        public int getCount() {
            return itemArray.size();
        }

        @Override
        public Object getItem(int position) {
            return itemArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemView view = new ListItemView(getApplicationContext());
            ListItem item = itemArray.get(position);
            view.setTitle(item.getTitle());
            view.setDesc(item.getDesc());

            return view;
        }
    }
}
