package com.mazzone.isere.transport.util;

import com.mazzone.isere.transport.interactor.Repo;
import com.mazzone.isere.transport.ui.model.SurfaceItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SurfaceManagerTest {

    // Mock repo
    private Repo<SurfaceItem> repo = new Repo<SurfaceItem>() {
        private List<SurfaceItem> surfaceItems = new ArrayList<>();

        @Override
        public long count() {
            return surfaceItems.size();
        }

        @Override
        public void insert(SurfaceItem object) {
            surfaceItems.add(object);
        }

        @Override
        public boolean exist(SurfaceItem object) {
            return surfaceItems.contains(object);
        }

        @Override
        public List<SurfaceItem> getAll() {
            return surfaceItems;
        }
    };

    @Before
    public void setUp() throws Exception {
        repo.insert(new SurfaceItem(0L, 0, 0, 10, 5));
        repo.insert(new SurfaceItem(1L, 0, 5, 10, 10));
        repo.insert(new SurfaceItem(2L, -10, -10, 0, 0));
        repo.insert(new SurfaceItem(3L, -10, -10, 10, 10));
    }

    @Test
    public void testNegativeBoundsCovered() throws Exception {
        SurfaceManager surfaceManager = new SurfaceManager(repo);
        assertEquals(surfaceManager.areBoundsCovered(6, 6, 4, 4), true);
    }

    @Test
    public void testBoundsCovered() throws Exception {
        SurfaceManager surfaceManager = new SurfaceManager(repo);
        assertEquals(surfaceManager.areBoundsCovered(4, 4, 5, 5), true);
    }

    @Test
    public void testBoundsCoveredNeg() throws Exception {
        SurfaceManager surfaceManager = new SurfaceManager(repo);
        assertEquals(surfaceManager.areBoundsCovered(-5, -5, 0, 0), true);
    }

    @Test
    public void testBoundsCoveredNegPos() throws Exception {
        SurfaceManager surfaceManager = new SurfaceManager(repo);
        assertEquals(surfaceManager.areBoundsCovered(-5, -5, 5, 5), true);
    }

    @Test
    public void testBoundsCoveredByMultiple() throws Exception {
        SurfaceManager surfaceManager = new SurfaceManager(repo);
        assertEquals(surfaceManager.areBoundsCovered(0, 0, 10, 10), true);
    }

    @Test
    public void testBoundsNotCovered() throws Exception {
        SurfaceManager surfaceManager = new SurfaceManager(repo);
        assertEquals(surfaceManager.areBoundsCovered(13, 13, 15, 15), false);
    }

    @Test
    public void testBoundsNotCoveredByMultiple() throws Exception {
        SurfaceManager surfaceManager = new SurfaceManager(repo);
        assertEquals(surfaceManager.areBoundsCovered(0, 0, 11, 10), false);
    }
}