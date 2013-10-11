package com.muzima.adapters.forms;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.muzima.api.model.Patient;
import com.muzima.api.model.PatientIdentifier;
import com.muzima.api.model.PersonName;
import com.muzima.controller.FormController;
import com.muzima.model.CompleteFormWithPatientData;
import com.muzima.model.collections.CompleteFormsWithPatientData;
import com.muzima.testSupport.CustomTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import java.util.concurrent.TimeUnit;

import static com.muzima.adapters.forms.CompleteFormsAdapter.BackgroundQueryTask;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(CustomTestRunner.class)
public class CompleteFormsAdapterTest {
    private CompleteFormsAdapter formsAdapter;
    private FormController formController;

    @Before
    public void setUp() throws Exception {
        formController = mock(FormController.class);
        formsAdapter = new CompleteFormsAdapter(null, 0, formController);

        Robolectric.getBackgroundScheduler().pause();
        Robolectric.getUiThreadScheduler().pause();
    }

    @Test
    public void queryTask_shouldFetchAllCompletedForms() throws Exception, FormController.FormFetchException {
        BackgroundQueryTask queryTask = new BackgroundQueryTask(formsAdapter);
        CompleteFormsWithPatientData completeFormsWithPatientData = new CompleteFormsWithPatientData();
        completeFormsWithPatientData.add(new CompleteFormWithPatientData());

        when(formController.getAllCompleteForms()).thenReturn(completeFormsWithPatientData);

        queryTask.execute();
        Robolectric.runBackgroundTasks();
        assertEquals(completeFormsWithPatientData, queryTask.get(100, TimeUnit.MILLISECONDS));
    }

    @Test
    public void shouldGroupPatients() throws Exception, FormController.FormFetchException {
        BackgroundQueryTask queryTask = new BackgroundQueryTask(formsAdapter);
        final Patient patient1 = patient("familyName", "middleName", "givenName", "identifier1");
        final Patient patient2 = patient("familyName", "middleName", "givenName", "identifier2");
        CompleteFormsWithPatientData completeFormsWithPatientData = new CompleteFormsWithPatientData() {{
            add(completeFormWithPatientData(patient1));
            add(completeFormWithPatientData(patient2));
            add(completeFormWithPatientData(patient1));
        }};
        when(formController.getAllCompleteForms()).thenReturn(completeFormsWithPatientData);
        StickyListHeadersListView listView = new StickyListHeadersListView(Robolectric.application);
        listView.setAdapter(formsAdapter);
        formsAdapter.setListView(listView);

        queryTask.execute();
        Robolectric.runBackgroundTasks();
        Robolectric.runUiThreadTasks();
        assertThat(formsAdapter.getPatients(), is(asList(patient1, patient2)));
    }

    private CompleteFormWithPatientData completeFormWithPatientData(final Patient patient1) {
        return new CompleteFormWithPatientData() {{
            setPatient(patient1);
        }};
    }

    private Patient patient(String familyName, String middleName, String givenName, String identifier) {
        Patient patient = new Patient();
        PersonName personName = new PersonName();
        personName.setFamilyName(familyName);
        personName.setMiddleName(middleName);
        personName.setGivenName(givenName);
        patient.setNames(asList(personName));
        PatientIdentifier personId = new PatientIdentifier();
        personId.setIdentifier(identifier);
        patient.setIdentifiers(asList(personId));
        return patient;
    }
}