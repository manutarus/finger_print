/*
 * Copyright (c) 2014. The Trustees of Indiana University.
 *
 * This version of the code is licensed under the MPL 2.0 Open Source license with additional
 * healthcare disclaimer. If the user is an entity intending to commercialize any application
 * that uses this code in a for-profit venture, please contact the copyright holder.
 */

/**
 * Copyright 2012 Muzima Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.muzima.adapters.cohort;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.muzima.MuzimaApplication;
import com.muzima.adapters.MuzimaPagerAdapter;
import com.muzima.controller.CohortController;
import com.muzima.view.cohort.AllCohortsListFragment;
import com.muzima.view.cohort.SyncedCohortsListFragment;

/**
 * Responsible to hold all the cohort fragments as multiple pages/tabs.
 */
public class CohortPagerAdapter extends MuzimaPagerAdapter {
    private static final String TAG = "CohortPagerAdapter";

    public static final int TAB_SYNCED = 0;
    public static final int TAB_All = 1;

    public CohortPagerAdapter(Context context, FragmentManager supportFragmentManager) {
        super(context, supportFragmentManager);
    }

    public void initPagerViews(){
        pagers = new PagerView[2];
        CohortController cohortController = ((MuzimaApplication) context.getApplicationContext()).getCohortController();

        AllCohortsListFragment allCohortsListFragment = AllCohortsListFragment.newInstance(cohortController);
        SyncedCohortsListFragment syncedCohortsListFragment = SyncedCohortsListFragment.newInstance(cohortController);

        allCohortsListFragment.setCohortDataDownloadListener(syncedCohortsListFragment);

        pagers[TAB_SYNCED] = new PagerView("Synced", syncedCohortsListFragment);
        pagers[TAB_All] = new PagerView("All", allCohortsListFragment);
    }

    public void onCohortDownloadStart() {
        ((AllCohortsListFragment)pagers[TAB_All].fragment).onCohortDownloadStart();
    }

    public void onCohortDownloadFinish() {
        ((AllCohortsListFragment)pagers[TAB_All].fragment).onCohortDownloadFinish();
    }

    public void onPatientsDownloadFinish() {
        ((AllCohortsListFragment)pagers[TAB_All].fragment).onPatientDownloadFinish();
    }

    public void reinitializeAllCohortsTab() {
        pagers[TAB_All].fragment.unselectAllItems();
        ((AllCohortsListFragment)pagers[TAB_All].fragment).endActionMode();
    }
}
