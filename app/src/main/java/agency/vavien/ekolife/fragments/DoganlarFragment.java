package agency.vavien.ekolife.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import agency.vavien.ekolife.R;
import agency.vavien.ekolife.fragments_doganlar.FragmentDoganlarOne;
import agency.vavien.ekolife.fragments_doganlar.FragmentDoganlarThree;
import agency.vavien.ekolife.fragments_doganlar.FragmentDoganlarTwo;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class DoganlarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_doganlar, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager_doganlar);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_doganlar);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(1);

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentDoganlarOne(), "Bugün");
        adapter.addFragment(new FragmentDoganlarTwo(), "Bu Hafta");
        adapter.addFragment(new FragmentDoganlarThree(), "Haftaya");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

