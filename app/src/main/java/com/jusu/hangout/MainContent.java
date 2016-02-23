package com.jusu.hangout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.session.PlaybackState;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainContent extends AppCompatActivity {

    ListView chatListView;
    ListView contactsListView;

    ImageView tapTab0;
    ImageView tapTab1;
    ImageView tapTab2;
    ImageView tapTab3;

    TextView changeFullName, changePassword,userName,fullName;

    String username = "";
    RelativeLayout chatLayout, contactsLayout, hangOutLayout, meLayout;

    ArrayList<HashMap<String, Object>> hashData;
    ArrayList<HashMap<String, Object>> contactsData;
    public static int contactcount = 0 ;
    public static String contacthold = "";

    public void clickFunction(View view) {

        ImageView counter = (ImageView) view;

//        System.out.println(counter.getTag().toString());
        int tappedTag = Integer.parseInt(counter.getTag().toString());

        if (tappedTag == 1) {
            tapTab1.setImageResource(R.mipmap.tabbar_contacts_hl);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab2.setImageResource(R.mipmap.tabbar_discover);
            tapTab3.setImageResource(R.mipmap.tabbar_me);
            contactsLayout.setVisibility(View.VISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
            hangOutLayout.setVisibility(View.INVISIBLE);
            meLayout.setVisibility(View.INVISIBLE);

        } else if (tappedTag ==2) {
            tapTab2.setImageResource(R.mipmap.tabbar_discover_hl);
            tapTab1.setImageResource(R.mipmap.tabbar_contacts);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab3.setImageResource(R.mipmap.tabbar_me);
            hangOutLayout.setVisibility(View.VISIBLE);
            contactsLayout.setVisibility(View.INVISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
            meLayout.setVisibility(View.INVISIBLE);
        } else if (tappedTag ==3) {
            tapTab3.setImageResource(R.mipmap.tabbar_me_hl);
            tapTab1.setImageResource(R.mipmap.tabbar_contacts);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab2.setImageResource(R.mipmap.tabbar_discover);
            meLayout.setVisibility(View.VISIBLE);
            hangOutLayout.setVisibility(View.INVISIBLE);
            contactsLayout.setVisibility(View.INVISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
        } else {
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe_hl);
            tapTab1.setImageResource(R.mipmap.tabbar_contacts);
            tapTab2.setImageResource(R.mipmap.tabbar_discover);
            tapTab3.setImageResource(R.mipmap.tabbar_me);
            chatLayout.setVisibility(View.VISIBLE);
            meLayout.setVisibility(View.INVISIBLE);
            hangOutLayout.setVisibility(View.INVISIBLE);
            contactsLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void pressToBroadcastLocation(View view) {
        Intent intent = new Intent(MainContent.this, LocationBroadcastActivity.class);
        startActivity(intent);
        finish();
    }

    public void changeAccountPassword(View view) {
        Intent intent = new Intent(MainContent.this, AccountSettings.class);
        intent.putExtra("settings", "password");
        startActivity(intent);
        finish();
    }

    public void changUserFullName(View view) {
        Intent intent = new Intent(MainContent.this, AccountSettings.class);
        intent.putExtra("settings", "fullname");
        startActivity(intent);
        finish();
    }

    public void logOut (View view) {

        SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage

        accountInfo.edit().putString("username", "").apply();        //CLean up user info
        accountInfo.edit().putString("password", "").apply();
        accountInfo.edit().putString("fullname", "").apply();
        accountInfo.edit().putString("sex", "").apply();

        Intent intent = new Intent(MainContent.this, LoginPage.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincontent);

        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage
//        String username = accountInfo.getString("username", "");                                                   //get account info in local storage
        userName = (TextView) findViewById(R.id.userNameTextView);
        fullName = (TextView) findViewById(R.id.fullNameTextView);
        userName.setText("username: " + accountInfo.getString("username", ""));
        fullName.setText(accountInfo.getString("fullname", ""));                //this has logic error, we need get the fullname from server


        tapTab0 = (ImageView)findViewById(R.id.img_mainframe);
        tapTab1 = (ImageView)findViewById(R.id.img_contacts);
        tapTab2 = (ImageView)findViewById(R.id.img_discover);
        tapTab3 = (ImageView)findViewById(R.id.img_me);

        chatLayout = (RelativeLayout) findViewById(R.id.chatLayout);
        contactsLayout = (RelativeLayout) findViewById(R.id.contactsLayout);
        hangOutLayout = (RelativeLayout) findViewById(R.id.hangOutLayout);
        meLayout = (RelativeLayout) findViewById(R.id.meLayout);


        chatListView = (ListView) findViewById(R.id.chatListView);
        contactsListView = (ListView) findViewById(R.id.contactsListView);

        changeFullName = (TextView) findViewById(R.id.changfullname);

        changePassword = (TextView) findViewById(R.id.changepassword);

        if (accountInfo.getString("fromsetting", "").equals("true") == true) {
                tapTab3.setImageResource(R.mipmap.tabbar_me_hl);
                tapTab1.setImageResource(R.mipmap.tabbar_contacts);
                tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
                tapTab2.setImageResource(R.mipmap.tabbar_discover);
                meLayout.setVisibility(View.VISIBLE);
                hangOutLayout.setVisibility(View.INVISIBLE);
                contactsLayout.setVisibility(View.INVISIBLE);
                chatLayout.setVisibility(View.INVISIBLE);
                accountInfo.edit().putString("fromsetting", "").apply();
        }


        final CustomSimpleAdapter customSimpleAdapter = new CustomSimpleAdapter( MainContent.this, getHashMapData(), R.layout.custom_list_layout);
        final ContactsAdapter customContactsAdapter = new ContactsAdapter( MainContent.this, getContactMapData(), R.layout.custom_list_layout);

        chatListView.setAdapter(customSimpleAdapter);

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("ID: ", String.valueOf(hashData.get(position).get("id")));
                Intent intent = new Intent(MainContent.this, ChatPage.class);
                intent.putExtra("numberid", String.valueOf(hashData.get(position).get("id")));
                intent.putExtra("name", String.valueOf(hashData.get(position).get("name")));
                startActivity(intent);
                finish();
            }
        });

        contactsListView.setAdapter(customContactsAdapter);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("ID: ", String.valueOf(hashData.get(position).get("id")));
                Intent intent = new Intent(MainContent.this, Contacts.class);
                intent.putExtra("numberid", String.valueOf(contactsData.get(position).get("id")));
                intent.putExtra("name", String.valueOf(contactsData.get(position).get("name")));
                startActivity(intent);
                finish();
            }
        });


//        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //Log.i("ID: ", String.valueOf(hashData.get(position).get("id")));
//
//                Log.i("id",String.valueOf(hashData.get(position).get("id")));
//
//                Intent intent = new Intent(MainContent.this, ChatPage.class);
//
////                intent.putExtra("numberid", (hashData.get(position).get("id")).toString());
//
//
////                intent.putExtra("name", (hashData.get(position).get("name")).toString());
//
//                startActivity(intent);
//            }
//        });


//        customSimpleAdapter = new CustomSimpleAdapter(MainContent.this, getHashMapData(), R.layout.custom_list_layout);

//        chatListView.setAdapter(customSimpleAdapter);



        /*------------ meLayout--------------*/

        //EditText username = (EditText) findViewById(R.id.username);
        System.out.println("debug");

        /*------------ meLayout--------------*/
    }

    // Define a Adapter by myself
    private class CustomSimpleAdapter extends SimpleAdapter {
        private Context context;
        private ArrayList<HashMap<String, Object>> data;
        private int layoutResource;

        //@param context
        //@param data
        //@param resource
        //@param from
        //@param to 构造函数

        public CustomSimpleAdapter(Context context,
                                   ArrayList<HashMap<String, Object>> data, int resource) {
            super(context, data, resource, null, null);
            this.context = context;
            this.data = data;
            this.layoutResource = resource;
        }


        class ViewHolder {
            ImageView picture;
            TextView number;
            TextView name;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.SimpleAdapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layoutView = layoutInflater.inflate(layoutResource, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.picture = (ImageView) layoutView.findViewById(R.id.imageViewLayout);
            viewHolder.number = (TextView) layoutView.findViewById(R.id.number);

            viewHolder.name = (TextView) layoutView.findViewById(R.id.name);
            viewHolder.picture.setImageResource(Integer.parseInt(data.get(position).get("imageView").toString()));
            viewHolder.number.setText(data.get(position).get("id").toString());
            Log.e("id", data.get(position).get("name").toString());
            viewHolder.name.setText(data.get(position).get("name").toString());
            return layoutView;
        }
    }

    private class ContactsAdapter extends SimpleAdapter {
        private Context context;
        private ArrayList<HashMap<String, Object>> data;
        private int layoutResource;

        public ContactsAdapter(Context context,
                               ArrayList<HashMap<String, Object>> data, int resource) {
            super(context, data, resource, null, null);
            this.context = context;
            this.data = data;
            this.layoutResource = resource;
        }
        class ViewHolder {
            ImageView picture;
            TextView number;
            TextView name;
        }
        /*
         * (non-Javadoc)
         *
         * @see android.widget.SimpleAdapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layoutView = layoutInflater.inflate(layoutResource, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.picture = (ImageView) layoutView.findViewById(R.id.imageViewLayout);
            viewHolder.number = (TextView) layoutView.findViewById(R.id.number);

            viewHolder.name = (TextView) layoutView.findViewById(R.id.name);
            viewHolder.picture.setImageResource(Integer.parseInt(data.get(position).get("imageView").toString()));
            viewHolder.number.setText(data.get(position).get("id").toString());
            Log.e("id", data.get(position).get("name").toString());
            viewHolder.name.setText(data.get(position).get("name").toString());
            return layoutView;
        }
    }



    //binding the data and images use the custom adapter defined by myself

    private ArrayList<HashMap<String, Object>> getHashMapData() {
        hashData = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> mItem = new HashMap<String, Object>();
            mItem.put("id", "Number：" + i);
            mItem.put("name", "Name" + i);
            switch (i % 5) {
                case 0:
                    mItem.put("imageView", R.drawable.text_people_1);
                    break;
                case 1:
                    mItem.put("imageView", R.drawable.text_people_2);
                    break;
                case 2:
                    mItem.put("imageView", R.drawable.text_people_3);
                    break;
                case 3:
                    mItem.put("imageView", R.drawable.text_people_4);
                    break;
                case 4:
                    mItem.put("imageView", R.drawable.text_people_5);
                    break;
                default:
                    mItem.put("imageView", R.drawable.text_people_6);
                    break;
            }
            hashData.add(mItem);
        }
        return hashData;

    }
    public static int count(String s, char c) {
        return s.length()==0 ? 0 : (s.charAt(0)==c ? 1 : 0) + count(s.substring(1),c);
    }

    private ArrayList<HashMap<String, Object>> getContactMapData() {
        contactsData = new ArrayList<HashMap<String, Object>>();
        Map<String, String> params = new HashMap<String, String>();
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);
        username = accountInfo.getString("username", "");
        System.out.println(username);
        params.put("user1", "ayoko001");
        final String json = new Gson().toJson(params);
        System.out.println(json);
        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts",json);
                    result = result.substring(1,result.length()-1);
                    System.out.println(result);
                    contactcount = count(result, ',');
                    for (int i = 0; i < contactcount+1; i++) {
                        HashMap<String, Object> mItem = new HashMap<String, Object>();
                        if (result.contains(":")){
                            contacthold= result.substring(1, result.indexOf(":")-1);
                        }
                        result = result.substring(result.indexOf(",")+1,result.length());
                        mItem.put("id", contacthold);
                        mItem.put("name", "contact:" + i);
                        switch (i % 5) {
                            case 0:
                                mItem.put("imageView", R.drawable.text_people_1);
                                break;
                            case 1:
                                mItem.put("imageView", R.drawable.text_people_2);
                                break;
                            case 2:
                                mItem.put("imageView", R.drawable.text_people_3);
                                break;
                            case 3:
                                mItem.put("imageView", R.drawable.text_people_4);
                                break;
                            case 4:
                                mItem.put("imageView", R.drawable.text_people_5);
                                break;
                            default:
                                mItem.put("imageView", R.drawable.text_people_6);
                                break;
                        }
                        contactsData.add(mItem);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();



        return contactsData;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy", "!!!!!!!!!!!!");
    }
}
