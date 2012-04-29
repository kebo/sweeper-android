package info.iamkebo.sweeper;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class SweeperActivity extends Activity {
	
	private ListView mList;
	private Button  mAddBtn;
	private EditText mInput;
	private DataHelper mData;
	private List<String> mNumbers;
	private ArrayAdapter<String> mAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mInput = (EditText)findViewById(R.id.input);
        mList = (ListView)findViewById(R.id.list);
        mAddBtn = (Button)findViewById(R.id.addBtn);
        mData = new DataHelper(this);
        mNumbers = mData.getAll();
        mAdapter = new ArrayAdapter<String>(this, 
        		android.R.layout.simple_list_item_1, 
        		mNumbers);
        
        mList.setAdapter(mAdapter);
        
        mAddBtn.setOnClickListener(addBtnListener);
        mList.setOnItemLongClickListener(itemSelectListener);
        
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	if(mData != null){
    		mData.cleanup();
    	}
    }
    
    private OnClickListener addBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String number = mInput.getText().toString();
			if(!mData.isExist(number)){
				mData.insert(number);
				mNumbers.add(number);
				mAdapter.notifyDataSetChanged();
				mInput.setText("");
			}
		}
	};
    
    private OnItemLongClickListener itemSelectListener  = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			String number = (String)parent.getItemAtPosition(position);
			int rows = mData.delete(number);
			if(rows > 0){
				mNumbers.remove(number);
				mAdapter.notifyDataSetChanged();
				return true;
			}
			return false;
		}
	};
    
    
}