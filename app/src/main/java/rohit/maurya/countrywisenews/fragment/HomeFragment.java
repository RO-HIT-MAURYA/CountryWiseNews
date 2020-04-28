package rohit.maurya.countrywisenews.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rohit.maurya.countrywisenews.R;

public class HomeFragment extends Fragment
{
    private View view;
    private Context context;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        }
        return view;
    }
}
