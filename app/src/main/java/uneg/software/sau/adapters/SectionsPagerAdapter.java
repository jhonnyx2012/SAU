package uneg.software.sau.adapters;

/**
 * Created by Jhonny on 25/2/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import uneg.software.sau.fragments.NoticiasFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final int area;
    private final NoticiasFragment fragmentNoticias;

    public SectionsPagerAdapter(FragmentManager fm,int area)
    {
        super(fm);
        this.area=area;
        fragmentNoticias=NoticiasFragment.newInstance(area);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            default: return fragmentNoticias;
            //default: return AlertarFragment.newInstance(position + 1);
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "NOTICIAS";
           // case 1:
           //     return "ALERTAR";
        }
        return null;
    }

    public void reloadWithhArea(int area) {
        ((NoticiasFragment)getItem(0)).setArea(area);
        ((NoticiasFragment)getItem(0)).onRefresh();
    }
}