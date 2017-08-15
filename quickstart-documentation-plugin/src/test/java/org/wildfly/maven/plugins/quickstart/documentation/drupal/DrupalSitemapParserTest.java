package org.wildfly.maven.plugins.quickstart.documentation.drupal;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.maven.plugin.logging.Log;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Jason Porter <jporter@redhat.com>
 *         Copyright 2017 Red Hat, Inc. and/or its affiliates.
 */
public class DrupalSitemapParserTest {
    @Mock
    Log mavenLog;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    InputStream sitemapInputstream = this.getClass().getClassLoader().getResourceAsStream("sitemap.xml");

    @Test
    public void testGetAllLocationsOfType() throws Exception {
        DrupalSitemapParser parser = new DrupalSitemapParser(sitemapInputstream, mavenLog);
        assertTrue(parser.getAllLocationsOfType("quickstarts").size() > 0);
        verifyZeroInteractions(mavenLog);
    }

    @Test
    public void testGetAllLocationsOfTypeContainsExpectedEntries() throws Exception {
        DrupalSitemapParser parser = new DrupalSitemapParser(sitemapInputstream, mavenLog);
        assertTrue(parser.getAllLocationsOfType("quickstarts").containsAll(Arrays.asList(
                new SitemapEntry("/quickstarts/eap/inter-app"),
                new SitemapEntry("/quickstarts/eap/bean-validation"),
                new SitemapEntry("/quickstarts/eap/cdi-decorator"))));
        verifyZeroInteractions(mavenLog);
    }

    @Test
    public void testGetAllLocationsOfTypeDoesNotContainUnexpectedEntries() throws Exception {
        DrupalSitemapParser parser = new DrupalSitemapParser(sitemapInputstream, mavenLog);
        final List<SitemapEntry> sitemapEntries = parser.getAllLocationsOfType("quickstarts");
        assertFalse(sitemapEntries.isEmpty());
        assertFalse(sitemapEntries.containsAll(Arrays.asList("/projects", "/ticket-monster", "/video/youtube/YeD7upQJoFc")));
        verifyZeroInteractions(mavenLog);
    }

    @Test
    public void testErrorParsingSitemap() {
        DrupalSitemapParser parser = new DrupalSitemapParser(null, mavenLog);
        final List<SitemapEntry> sitemapEntries = parser.getAllLocationsOfType("quickstarts");
        assertTrue(sitemapEntries.isEmpty());
        verify(mavenLog, atMost(1)).error(isA(XMLStreamException.class));
    }

}