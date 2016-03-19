package gagan.com.communities.activites;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;

import org.json.JSONObject;

import gagan.com.communities.R;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class SelectUsersListActivity extends BaseActivityG {


    private RecyclerView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_users_list);


        settingActionBar();
        findViewByID();

        hitWebserviceG();
    }






    private void settingActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_user_community, menu);

        menu.findItem(R.id.done).setTitle(Html.fromHtml("<font color='#0000'>Done</font>"));
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    void findViewByID() {
        listview = (RecyclerView) findViewById(R.id.listview);
    }

    @Override
    void hitWebserviceG()
    {
        try {
            JSONObject data = new JSONObject();
            data.put("user_id", sharedPrefHelper.getUserId());
            data.put("f_status", "1");  // 1 for followers 2 for following.


            new SuperWebServiceG(GlobalConstants.URL + "getfollowerList", data, new CallBackWebService() {
                @Override
                public void webOnFinish(String output) {


                    processOutput(output);

                }
            }).execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void processOutput(String output) {
        try {

            JSONObject jsonMain = new JSONObject(output);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201"))
            {



            }
            else {
                Utills.showToast("No users available", SelectUsersListActivity.this, true);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }









   /* public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolderG>

    {
        private LayoutInflater inflater;

        Context con;

        private List<String> dataList;


        private List<String> dataListG;

        public ContactsAdapter(Context context, List<String> dList)
        {

            this.dataList = dList;
            con = context;
            inflater = LayoutInflater.from(context);

            dataListG = new ArrayList<>();
        }

        @Override
        public MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = inflater.inflate(R.layout.contacts_inflator, parent, false);
            return new MyViewHolderG(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolderG holder, int position)
        {

            final String current = dataList.get(position);
            holder.title.setText(current.getName());
            holder.phoneNo.setText(current.getPhoneNumber());


            holder.chkBox.setChecked(true);

            holder.chkBox.setTag(position);

            holder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    CheckBox chkBoxx = (CheckBox) compoundButton;

                    if (b)
                    {
                        if (!dataListG.contains(dataList.get((int) chkBoxx.getTag())))
                        {
                            dataListG.add((String) chkBoxx.getTag());
                        }

                    }
                    else
                    {
                        dataListG.remove(dataList.get((int) chkBoxx.getTag()));
                    }
                }
            });


            holder.view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("data", current);

                    ((Activity) con).setResult(Activity.RESULT_OK, resultIntent);
                    ((Activity) con).finish();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }


        class MyViewHolderG extends RecyclerView.ViewHolder
        {
            TextView title, phoneNo;
            View view;
            ImageView icon;
            CheckBox chkBox;

            public MyViewHolderG(View itemView)
            {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.name);
                phoneNo = (TextView) itemView.findViewById(R.id.phone);
                icon = (ImageView) itemView.findViewById(R.id.image);
                chkBox = (CheckBox) itemView.findViewById(R.id.image);
                view = itemView;
            }
        }

    }*/


}
