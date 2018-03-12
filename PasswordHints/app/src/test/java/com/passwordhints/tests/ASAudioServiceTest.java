package com.passwordhints.tests;

import android.content.Context;
import com.passwordhints.services.ASAudioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ASAudioServiceTest {

    @Mock
    private Context c;

    @Test
    public void startRecordingTest() {
        when(c.getCacheDir()).thenReturn(new File("/fake/cache"));
        when(c.getFilesDir()).thenReturn(new File("fake/files"));
        ASAudioService as = new ASAudioService(c);
    }
}