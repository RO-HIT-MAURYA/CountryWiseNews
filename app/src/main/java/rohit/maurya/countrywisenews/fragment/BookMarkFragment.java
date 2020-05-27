package rohit.maurya.countrywisenews.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.databinding.FragmentBookmarkBinding;

public class BookMarkFragment extends Fragment
{
    FragmentBookmarkBinding fragmentBookmarkBinding;

    public static BookMarkFragment newInstance() {
        BookMarkFragment fragment = new BookMarkFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (fragmentBookmarkBinding == null)
        {
            fragmentBookmarkBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_bookmark,container,false);
        }
        // Inflate the layout for this fragment
        return fragmentBookmarkBinding.getRoot();
    }
}
