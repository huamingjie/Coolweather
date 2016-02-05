package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB
{
	public static final String DB_NAME = "cool_weather";
	
	public static final int VERSION = 1;
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	
	private CoolWeatherDB(Context context)
	{
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	public synchronized static CoolWeatherDB getInstance(Context context)
	{
		if(coolWeatherDB == null)
		{
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/*
	 * 将province实例存储到数据库
	 * 
	 */
	public void saveProvince(Province province)
	{
		if(province != null)
		{
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Provice", null, values);
		}
	}
	
	/*
	 * 从数据库读取全国所有的省份信息
	 */
	public List<Province> loadProvinces()
	{
		List<Province> list_province = new ArrayList<Province>();
		Cursor cursor = db.query("Province",null,null,null,null,null,null);
		if(cursor.moveToFirst())
		{
			do
			{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list_province.add(province);
			}
			while(cursor.moveToNext());
		}
		if(cursor != null)
		{
			cursor.close();
		}
		return list_province;
	}
	
	/*
	 * 将City实例存储到数据库
	 */
	public void saveCity(City city)
	{
		if(city != null)
		{
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	
	/*
	 * 读取某个省下的城市信息
	 */
	public List<City> loadCitiesByProvinceId(int provinceId)
	{
		List<City> list_city = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				City city = new City();
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceId(provinceId);
				list_city.add(city);
			}
			while(cursor.moveToNext());
		}
		if(cursor != null)
		{
			cursor.close();
		}
		
		return list_city;
	}
	
	/*
	 * 将county实例存储到数据库
	 */
	public void saveCounty(County county)
	{
		if(county != null)
		{
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	/*
	 * 读取某城市下的所有县的信息
	 */
	public List<County> loadCoutiesByCityId(int cityId)
	{
		List<County> list_county = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				County county = new County();
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("city_code")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("city_name")));
				county.setCityId(cityId);
				list_county.add(county);
			}
			while(cursor.moveToNext());
		}
		if(cursor != null)
		{
			cursor.close();
		}
		
		return list_county;
	}
	
}
