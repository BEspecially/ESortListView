package com.example.sortlistview;

import java.util.ArrayList;
import com.banlei.esortlistview.R;
import com.example.sortlistview.lib.StickyListHeadersAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class TestBaseAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

	private final Context mContext;
	private String[] mCountries;
	
	private int[] mSectionIndices;
	private Character[] mSectionLetters;
	
	private LayoutInflater mInflater;
	private CharacterParser characterParser;

	public TestBaseAdapter(Context context,String[] str) {
		characterParser = CharacterParser.getInstance();
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mCountries = str;
		
		mSectionIndices = getSectionIndices();
		mSectionLetters = getSectionLetters();
	}
	private int[] getSectionIndices() {
		ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
		//ȡ��һ����ĸ
		char lastFirstChar = characterParser.getSelling(mCountries[0]).toUpperCase().charAt(0);
		
		sectionIndices.add(0);
		for (int i = 1; i < mCountries.length; i++) {
			char ping=characterParser.getSelling(mCountries[i]).toUpperCase().charAt(0);
			if ( ping!= lastFirstChar) {
				lastFirstChar = ping;
				sectionIndices.add(i);
			}
		}
		//����sectionIndices����洢��id
		int[] sections = new int[sectionIndices.size()];
		for (int i = 0; i < sectionIndices.size(); i++) {
			sections[i] = sectionIndices.get(i);
		}
		return sections;
	}
	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	public int getPositionForSectionss(char section) {
		for (int i = 0; i < mCountries.length; i++) {
			char pinyin1 = characterParser.getSelling(mCountries[i]).toUpperCase().charAt(0);
			if (pinyin1==section) {
				return i;
			}
		}
		return -1;
	}

	private Character[] getSectionLetters() {
		Character[] letters = new Character[mSectionIndices.length];
		for (int i = 0; i < mSectionIndices.length; i++) {
			letters[i] = characterParser.getSelling(mCountries[mSectionIndices[i]]).charAt(0);
		}
		return letters;
	}

	@Override
	public int getCount() {
		return mCountries.length;
	}

	@Override
	public Object getItem(int position) {
		return mCountries[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.test_list_item_layout,
					parent, false);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text.setText(mCountries[position]);

		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;

		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.header, parent, false);
			holder.text = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		CharSequence headerChar = mCountries[position].subSequence(0, 1);
		
		//TODO
		//��ȡ����ĸ
		//����ת����ƴ��
		String pinyin = characterParser.getSelling(headerChar.toString());
		String sortString = pinyin.substring(0, 1).toUpperCase();
			
		holder.text.setText(sortString);
		return convertView;
	}
	class HeaderViewHolder {
		TextView text;
	}

	class ViewHolder {
		TextView text;
	}
	/**
	 * ����������߱�Ǹ���headerView��һ��������������ͬID�Ľ�����ʾΪͬһView
	 */
	@Override
	public long getHeaderId(int position) {
		char charAt = characterParser.getSelling(mCountries[position]).toUpperCase().charAt(0);
//		char charAt1 = mCountries[position].subSequence(0, 1).charAt(0);
		return charAt;
	}
 
	@Override
	public int getPositionForSection(int section) {
		if (section >= mSectionIndices.length) {
			section = mSectionIndices.length - 1;
		} else if (section < 0) {
			section = 0;
		}
		return mSectionIndices[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		for (int i = 0; i < mSectionIndices.length; i++) {
			if (position < mSectionIndices[i]) {
				return i - 1;
			}
		}
		Log.e("mSectionIndices.length - 1", mSectionIndices.length - 1+"");
		return mSectionIndices.length - 1;
	}

	@Override
	public Object[] getSections() {
		return mSectionLetters;
	}
 

}
