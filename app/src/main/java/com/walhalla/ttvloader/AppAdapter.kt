package com.walhalla.ttvloader

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.android.widget.AppViewHolder
import com.walhalla.ConfigUtils
import com.walhalla.abcsharedlib.SharedNetwork
import com.walhalla.adapters.AppModel
import com.walhalla.ttvloader.databinding.ItemAppBinding
import com.walhalla.ttvloader.mvp.MainView
import com.walhalla.ttvloader.utils.IntentUtils
import com.walhalla.ui.DLog
import com.walhalla.ui.UConst
import com.walhalla.ui.plugins.Launcher
import com.walhalla.ui.utils.PackageUtils
import java.lang.reflect.Field
import java.util.List

class AppAdapter : RecyclerView.Adapter<AppViewHolder> {
    private lateinit var data: MutableList<AppModel>
    private lateinit var mainView: MainView
    private lateinit var context: Context
    //    public AppAdapter(MutableList<AppModel> data, MainView mainView) {
    //        this.data = data;
    //        this.mainView = mainView;
    //    }
    constructor(context: Context, mainView: MainView) {
        this.data = ConfigUtils.makeList(context)
        this.mainView = mainView
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.getContext()!!)
        var view: ItemAppBinding = ItemAppBinding.inflate(inflater, parent, false)
        return AppViewHolder(view)
    }
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        var item: AppModel = data.`get`(position)
        holder.bind(item)
        holder.binding!!.overflowMenu.setOnClickListener({
                view -> showPopupMenu(view, item, position)
                })
        holder.binding!!.bankcardId.setOnClickListener({ v -> 
                appLaunch(context, item)
                })
    }
    private fun showPopupMenu(v: View, item: AppModel, adapterPosition: Int) {
        var popup: PopupMenu = PopupMenu(v.getContext()!!, v)
        var inflater: MenuInflater = popup.getMenuInflater()
        var menu: Menu = popup.getMenu()
        //        MenuPopupHelper menuHelper = new MenuPopupHelper(
        //                mView.getActivity()!!, (MenuBuilder) menu, v);
        //        menuHelper.setForceShowIcon(true);
        //        menuHelper.show();
        //reaper.wrapper(menu, new File(packageInfo.applicationInfo.sourceDir));
        inflater.inflate(R.menu.abc_popup_app, menu)
        // Dynamically add menu items
        menu.add(Menu.NONE, R.id.action_open_link, 101, R.string.open_on_google_play).setIcon(R.drawable.ic_action_open_link).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        if (PackageUtils.isPackageInstalledForLaunch(context, item.appPackageName)) {
            menu.add(Menu.NONE, R.id.action_app_info, 105, R.string.action_app_info).setIcon(R.drawable.ic_info_outline_black_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
        var menuHelper: Any? = null
        var argTypes: Array<Class<*>>? = null
        try {
            @SuppressLint("DiscouragedPrivateApi")
            var fMenuHelper = PopupMenu::class.java.getDeclaredField("mPopup")
            fMenuHelper.isAccessible = true
            menuHelper = fMenuHelper.get(popup)
            argTypes = arrayOf(Boolean::class.javaPrimitiveType!!)
            if (menuHelper != null) {
                menuHelper!!.javaClass.getDeclaredMethod("setForceShowIcon", *argTypes!!).invoke(menuHelper!!, true)
            }
        } catch (ignored: Exception) {
        }
        popup.setOnMenuItemClickListener(lmb1@{ menuItem -> 
                var itemId: Int = menuItem.getItemId()
                if (itemId == R.id.action_open_link) {
                    Launcher.openMarketApp(v.getContext()!!, item.appPackageName)
                    return@lmb1 true
                } else if (itemId == R.id.action_app_info) {
                    IntentUtils.openSettingsForPackageName(context, item.appPackageName)
                    return@lmb1 true
                } else {
                    // Uncomment and handle the additional case if needed
                    // } else if (itemId == R.id.action_share_link) {
                    //     // Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    //     // getPresenter().onItemClicked(menuItem.getItemId(), category);
                    //     return true;
                    return@lmb1 false
                }
                })
        popup.show()
    }
    fun appLaunch(context: Context, item0: AppModel) {
        if (item0.appPackageName == null) {
            Launcher.openBrowser(context, "https://google.com")
            return
        }
        var packageName: String = item0.appPackageName
        if (PackageUtils.isPackageInstalledForLaunch(context, packageName)) {
            try {
                //Intent intent = null;
                when (packageName) {
                    SharedNetwork.Package.FACEBOOK, SharedNetwork.Package.FACEBOOKLITE -> {
                        //uri = "fb://facewebmodal/f?href=" + "111";
                        //                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("facebook:/newsfeed"));
                        //                        intent.setPackage(action); //Ok but need first launch
                        launchApp(context, packageName)
                    }
                    SharedNetwork.Package.TIKTOK_T_PACKAGE -> {
                        //Not Work in 10 --> intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vm.tiktok.com/"));
                        //Not Work in 10 --> intent.setPackage(action);
                        launchApp(context, packageName)
                    }
                    SharedNetwork.Package.TIKTOK_M_PACKAGE -> {
                        //Problem
                        launchApp(context, packageName)
                    }
                    SharedNetwork.Package.INSTAGRAM -> {
                        //                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"));
                        //                        intent.setPackage(item);//Ok but need first launch
                        launchApp(context, packageName)
                    }
                    SharedNetwork.Package.TWITTER -> {
                        //intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id="));
                        //intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=NASA"));
                        //intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=twitterapi"));
                        //                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"));
                        //                        intent.setPackage(action);//Ok but need first launch
                        launchApp(context, packageName)
                    }
                    SharedNetwork.Package.TRILLER, SharedNetwork.Package.PINTERESTLITE, SharedNetwork.Package.PINTEREST, SharedNetwork.Package.LIKEE -> {
                        //                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://s.like.video"));
                        //                        intent.setPackage(item);//Ok but need first launch
                        launchApp(context, packageName)
                    }
                    else -> {
                        //                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse());
                        //                        intent.setPackage(item);
                        launchApp(context, packageName)
                    }
                }
            } catch (e: Exception) {
                //                if (intent != null) {
                //                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //                    context.startActivity(intent);
                //                }
                //iUtils.ShowToast(getContext()!!, "Error occurred!");
                if (mainView != null) {
                    mainView.handleException(e)
                }
            } catch (r: Exception) {
                if (mainView != null) {
                    mainView.handleException(r)
                }
            }
        } else {
            //iUtils.ShowToast(getContext()!!, "Install facebook recyclerView");
            if (mainView != null) {
                var msg: String = String.format(context.getString(R.string.err_install_app), context.getString(item0.appName).uppercase())
                mainView.makeToaster0(msg)
            }
            //Launcher.openMarketApp(context, packageName);
            try {
                var uri: Uri = Uri.parse(UConst.MARKET_CONSTANT + packageName)
                var intent: Intent = Intent(Intent.ACTION_VIEW, uri)
                //intent.setPackage(PKG_NAME_VENDING);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.getApplicationContext().startActivity(intent)
            } catch (e: Exception) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(UConst.GOOGLE_PLAY_CONSTANT + packageName)))
            }
        }
    }
    private fun launchApp(context: Context, packageName: String) {
        var packageManager: PackageManager = context.getPackageManager()
        try {
            /*
                            public abstract Intent getLaunchIntentForPackage (String packageName)
                                Returns a "good" intentForPackage to launch a front-door activity in a package. This is used,
                                for example, to implement an "open" button when browsing through packages.
                                The current implementation looks first for a main activity in the category
                                CATEGORY_INFO, and next for a main activity in the category CATEGORY_LAUNCHER.
                                Returns null if neither are found.
            
                            Parameters
                                packageName : The name of the package to inspect.
                            Returns
                                A fully-qualified Intent that can be used to launch the main activity in the
                                package. Returns null if the package does not contain such an activity,
                                or if packageName is not recognized.
                        */
            // Initialize a new Intent
            val intentForPackage = packageManager.getLaunchIntentForPackage(packageName)
            /*
                         public Intent addCategory (String category)
                                Add a new category to the intentForPackage. Categories provide additional detail about
                                the action the intentForPackage performs. When resolving an intentForPackage, only activities that
                                provide all of the requested categories will be used.
            
                            Parameters
                                category : The desired category. This can be either one of the predefined
                                Intent categories, or a custom category in your own namespace.
                            Returns
                                Returns the same Intent object, for chaining multiple calls into a single statement.
                        */
            /*
                            public static final String CATEGORY_LAUNCHER
                                Should be displayed in the top-level launcher.
            
                                Constant Value: "android.intentForPackage.category.LAUNCHER"
                        */
            if (intentForPackage == null) {
                // Throw PackageManager NameNotFoundException
                throw PackageManager.NameNotFoundException()
            } else {
                // Start the app
                // Add category to intentForPackage
                intentForPackage.addCategory(Intent.CATEGORY_LAUNCHER)
                context.startActivity(intentForPackage)
            }
        } catch (e: Exception) {
            DLog.handleException(e)
            if (mainView != null) {
                mainView.makeErrorToaster(R.string.access_error)
            }
        }
    }
    override fun getItemCount(): Int {
        return data.size
    }
}
