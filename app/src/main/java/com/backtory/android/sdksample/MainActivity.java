package com.backtory.android.sdksample;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.backtory.androidsdk.internal.BacktoryCallBack;
import com.backtory.androidsdk.model.BacktoryResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;


public class MainActivity extends AppCompatActivity {

  static String lastGenEmail = "";
  static String lastGenUsername = "";
  static String lastGenPassword = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Get the ViewPager and set it's PagerAdapter so that it can display items
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
        MainActivity.this));

    // Give the TabLayout the ViewPager
    TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
    tabLayout.setupWithViewPager(viewPager);

    PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
  }


  //-----------------------------------------------------------------------------

  static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

  static String generateEmail(boolean random) {
    String s = random ? randomAlphabetic(3) + "@" + randomAlphabetic(3) + ".com" : "ar.d.farahani@gmail.com";
    lastGenEmail = s;
    return s;
  }

  static String generateUsername(boolean random) {
    String s = random ? randomAlphabetic(6) : "hamze";
    lastGenUsername = s;
    return s;
  }

  static String generatePassword(boolean random) {
    String s = random ? randomAlphabetic(6) : "1234";
    lastGenPassword = s;
    return s;
  }

  //-----------------------------------------------------------------------------
  public abstract static class AbsFragment extends Fragment {
    @BindView(R.id.textview)
    TextView textView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View v = inflater.inflate(getLayoutRes(), container, false);
      unbinder = ButterKnife.bind(this, v);
      return v;
    }

    protected abstract
    @LayoutRes
    int getLayoutRes();

    protected <T> BacktoryCallBack<T> printCallBack() {
      return new BacktoryCallBack<T>() {
        @Override
        public void onResponse(BacktoryResponse<T> response) {
          if (response.isSuccessful())
            textView.setText(gson.toJson(response.body()));
          else
            textView.setText(response.message());
        }
      };
    }

    void toast(String message) {
      Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
      super.onDestroyView();
      unbinder.unbind();
    }
  }

  //---------------------------------------------------------------------------


  public static class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Auth", "Lambda", "Game"};
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
      super(fm);
      this.context = context;
    }

    @Override
    public int getCount() {
      return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return new AuthFragment();
        case 1:
          return new CloudCodeFragment();
        case 2:
          return new GameFragment();
      }
      return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      // Generate title based on item position
      return tabTitles[position];
    }
  }
}