package it.giacomos.android.wwwsapp.widgets;

import java.lang.String;

public interface StateRestorer {
	boolean restoreFromInternalStorage();
	boolean isRestoreSuccessful();
	
	String makeFileName();

}
