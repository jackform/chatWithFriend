package org.jackform.innocent.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jackform.innocent.R;
import org.jackform.innocent.activity.MainTabActivity;
import org.jackform.innocent.utils.DebugLog;
import org.jackform.innocent.adapter.PlugListViewAdapter;

import java.io.File;
import java.util.Collection;

import androidx.pluginmgr.PlugInfo;
import androidx.pluginmgr.PluginManager;

/**
 * Created by jackform on 15-8-24.
 */
public class DynamicLoadFragment extends BaseFragment {
    private Context mContext;

    private ListView pluglistView;
    //
    private PluginManager plugMgr;
    private static final String sdcard = Environment
            .getExternalStorageDirectory().getPath();
    @Override
    public boolean isUse() {
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        Log.v("hahaha", "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        DebugLog.v("onDetach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_plag_in, null);
        final EditText pluginDirTxt = (EditText) view.findViewById(R.id.pluginDirTxt);
        Button pluginLoader = (Button) view.findViewById(R.id.pluginLoader);
        pluglistView = (ListView) view.findViewById(R.id.pluglist);

        plugMgr = PluginManager.getInstance(mContext);

        String pluginSrcDir = sdcard + "/download";
        pluginDirTxt.setText(pluginSrcDir);

        pluglistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                plugItemClick(position);
            }
        });

        final Context context = mContext;
        pluginLoader.setOnClickListener(new View.OnClickListener() {
            private volatile boolean plugLoading = false;

            @Override
            public void onClick(View v) {
                final String dirText = pluginDirTxt.getText().toString().trim();
                if (TextUtils.isEmpty(dirText)) {
                    Toast.makeText(context, getString(R.string.pl_dir),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (plugLoading) {
                    Toast.makeText(context, getString(R.string.loading),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                String strDialogTitle = getString(R.string.dialod_loading_title);
                String strDialogBody = getString(R.string.dialod_loading_body);
                final ProgressDialog dialogLoading = ProgressDialog.show(
                        context, strDialogTitle, strDialogBody, true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        plugLoading = true;
                        try {
                            Collection<PlugInfo> plugs = plugMgr
                                    .loadPlugin(new File(dirText));
                            setPlugins(plugs);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            dialogLoading.dismiss();
                        }
                        plugLoading = false;
                    }
                }).start();
            }
        });
        return view;
    }


    private void plugItemClick(int position) {
        PlugInfo plug = (PlugInfo) pluglistView.getItemAtPosition(position);
        plugMgr.startMainActivity(mContext, plug.getPackageName());
    }

    private void setPlugins(final Collection<PlugInfo> plugs) {
        if (plugs == null || plugs.isEmpty()) {
            return;
        }
        final ListAdapter adapter = new PlugListViewAdapter(mContext, plugs);
        ((MainTabActivity)mContext).runOnUiThread(new Runnable() {
            public void run() {
                pluglistView.setAdapter(adapter);
            }
        });
    }
}
