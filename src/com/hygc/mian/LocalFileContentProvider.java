
package com.hygc.mian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.android.FileBrowser.FileUtil;
import com.android.FileBrowser.StringUtils;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class LocalFileContentProvider extends ContentProvider {
	private static final String URI_PREFIX = "content://com.andorid.shu.love";
	/** 基本数据库存放路径**/
    public static final String BASE_JS_PATH = "/data/data/com.andorid.shu.love/img";
    public String jsName = "ichart.js";

    private String jsPath;
	public static String constructUri(String url) {
		Uri uri = Uri.parse(url);
		return uri.isAbsolute() ? url : URI_PREFIX + url;
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		File file = new File(uri.getPath());
		ParcelFileDescriptor parcel = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
		return parcel;
	}

	@Override
	public AssetFileDescriptor openAssetFile (Uri uri, String mode) throws FileNotFoundException{
        AssetManager am = getContext().getAssets();  
        String path = uri.getPath().substring(1);  
        Log.e("path:", path);
        
        //sdcard里有没有
        String tpath = "/sdcard/andych008/"+path;
        File file = new File(tpath);
		if (file.exists()) {
			Log.e("path2:", tpath);
			Uri turi = Uri.parse(URI_PREFIX+tpath);
			return super.openAssetFile(turi, mode);
		}
		
		//C盘有没有
         tpath = "/data/data/com.andorid.shu.love/"+path;
         file = new File(tpath);
		if (file.exists()) {
			Log.e("path2:", tpath);
			Uri turi = Uri.parse(URI_PREFIX+tpath);
			return super.openAssetFile(turi, mode);
		}
		
		try {
			AssetFileDescriptor afd = am.openFd(path);
			return afd;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return super.openAssetFile(uri, mode);
	}


	@Override
	public boolean onCreate() {
		//System.out.println("-------------adsad");
		jsPath = getDdPathPostFix();
		Log.v("sky.liu", "sky.liu for the js path,in oncreate"+jsPath);
		if (!StringUtils.isEmpty(jsPath)) {
			File databases_dir = new File(jsPath + jsName);
			if (!databases_dir.exists()) {
				Log.v("sky.liu","sky.liu js文件不存在开始创建文件");
				createFile();
			}
		}
		
		return true;
	}
	
	private void createFile() {
		File file_dir = new File(jsPath);
		
		if (!file_dir.exists()) {
			file_dir.mkdirs();
		}
		File js_file = new File(file_dir, jsName);
		if (!js_file.exists()) {
			try{
				js_file.createNewFile();
				 FileUtil fileUtil = new FileUtil();
			        AssetManager am = getContext().getAssets();
					InputStream is = am.open("jquery-2.1.1.min.js");
					fileUtil.copyFile(is, jsPath + "jquery-2.1.1.min.js");	
					is = am.open("ichart.js");
					fileUtil.copyFile(is, jsPath + "ichart.js");			
					is = am.open("HtmlPageCout.js");
					fileUtil.copyFile(is, jsPath + "HtmlPageCout.js");
					Runtime.getRuntime().exec( "chmod 777 " + jsPath + jsName);
			}catch(Exception e){
				e.printStackTrace();
				Log.e("sky.liu","sky.liu拷贝数据库出错"+e.getLocalizedMessage());
			}
		}
	}

	@Override
	public int delete(Uri uri, String s, String[] as) {
		throw new UnsupportedOperationException("Not supported by this provider");
	}

	@Override
	public String getType(Uri uri) {
		throw new UnsupportedOperationException("Not supported by this provider");
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentvalues) {
		throw new UnsupportedOperationException("Not supported by this provider");
	}

	@Override
	public Cursor query(Uri uri, String[] as, String s, String[] as1, String s1) {
		throw new UnsupportedOperationException("Not supported by this provider");
	}

	@Override
	public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {
		throw new UnsupportedOperationException("Not supported by this provider");
	}
	private String getDdPathPostFix() {
		String path = "";
		path = BASE_JS_PATH + "/";
		return path;
	}

}
