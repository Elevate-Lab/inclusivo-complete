package com.dsciiita.inclusivo.fragments.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.CompanyListingActivity;
import com.dsciiita.inclusivo.activities.CompanyProfileActivity;
import com.dsciiita.inclusivo.activities.EventInfoActivity;
import com.dsciiita.inclusivo.adapters.DashboardEventRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentEventsBinding;
import com.dsciiita.inclusivo.models.Event;
import com.dsciiita.inclusivo.models.JobFilterBody;
import com.dsciiita.inclusivo.models.JobFilterObject;
import com.dsciiita.inclusivo.models.JobFilterSearch;
import com.dsciiita.inclusivo.responses.FilterCompanyResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    FragmentEventsBinding binding;

    private DashboardEventRVAdapter eventRvAdapter;
    private List<Event> events;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater);

        setupAdapters();

        getData();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void setupAdapters() {
        eventRvAdapter = new DashboardEventRVAdapter(Objects.requireNonNull(getContext()), events, onclickListener);
        binding.eventRv.setAdapter(eventRvAdapter);

        binding.refreshLayout.setOnRefreshListener(this::getData);
    }

    private void getData() {

        int index = (int) (Math.random() * (9));
        binding.quote.setText((getResources().getStringArray(R.array.diversity_quotes))[index]);
        binding.progressLayout.setVisibility(View.VISIBLE);
        binding.progressBar.setFrame(30);
        binding.prent.setVisibility(View.GONE);
        binding.prent.setAlpha(0);
        binding.errorView.setVisibility(View.GONE);

        getEvents();
    }

    private void getEvents() {
        events = new ArrayList<>();
        Event event = new Event(1, "MLH Fellowship Talk", "We pair fun, educational curriculum with real-world practical experience. It's collaborative, remote, & happens under the guidance of expert mentors. You'll collaborate on projects that are sourced directly from our corporate partners. Whether you're contributing to a major Open Source technology or a new greenfield project, you'll be solving a real-world need with technologies that employers care about. We offer fellows an educational stipend to help offset expenses while they participate in the program. You'll be able to focus on putting what you learn into practice so you can launch your career in Software Engineering once you graduate.",
                "Online", "Sunday 27 June 2021 20:00", "https://i.ytimg.com/vi/ffsh-05TZaY/hq720.jpg");
        Event event2 = new Event(2, "Hacktoberfest'20: Kickstart your Opensource Journey", "Hacktoberfest is a monthlong celebration of open source software run by DigitalOcean. Hacktoberfest is open to everyone in our global community! Four quality pull requests must be submitted to public GitHub repositories. You can sign up anytime between October 1 and October 31. First, register at hacktoberfest.digitalocean.com. Then, submit at least four pull requests to any public GitHub repository that is classified with the hacktoberfest topic or has a hacktoberfest-accepted label on it. You can look for open issues labeled Hacktoberfest for inspiration. Quality contributions are encouraged! Are you maintaining a repo? Create issues or classify existing issues on your GitHub projects with hacktoberfest to help let new contributors know what to work on. Tag any spam or irrelevant pull requests with the invalid label to disqualify them.",
                "Online", "Sunday 27 June 2021 20:00", "https://i.ytimg.com/vi/XKb57AYgXwo/hq720.jpg");
        events.add(event);
        events.add(event2);

        eventRvAdapter.updateAdapter(events);

        binding.progressLayout.setVisibility(View.GONE);
        binding.refreshLayout.setRefreshing(false);
        binding.prent.setVisibility(View.VISIBLE);
        binding.prent.animate().alpha(1).setDuration(300);
    }


    DashboardEventRVAdapter.OnclickListener onclickListener = new DashboardEventRVAdapter.OnclickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(getActivity(), EventInfoActivity.class).putExtra("event", new Gson().toJson(events.get(position))));
        }

        @Override
        public void onLongClick(int position) {

        }
    };
}