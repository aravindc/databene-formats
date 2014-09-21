/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.formats.fixedwidth;

import org.junit.Test;
import static junit.framework.Assert.*;

import java.io.StringReader;
import java.util.Arrays;

import org.databene.commons.SystemInfo;
import org.databene.commons.format.Alignment;
import org.databene.commons.format.PadFormat;
import org.databene.formats.DataContainer;

/**
 * Tests the {@link FixedWidthLineIterator}.<br/>
 * <br/>
 * Created: 27.08.2007 07:20:05
 * @author Volker Bergmann
 */
public class FixedWidthLineIteratorTest {

    private static final String SEP = SystemInfo.getLineSeparator();

    @Test
    public void testProcessingEmptyLines() throws Exception {
        FixedWidthLineIterator iterator = createIterator(true);
        DataContainer<String[]> container = new DataContainer<String[]>();
        assertTrue(Arrays.equals(new String[] {"Alice" , "23"}, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] {"Bob"   , "34"}, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] {"Charly", "45"}, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] {"Dieter", "-1"}, iterator.next(container).getData()));
    }

    @Test
    public void testIgnoringEmptyLines() throws Exception {
        FixedWidthLineIterator iterator = createIterator(false);
        DataContainer<String[]> container = new DataContainer<String[]>();
        assertTrue(Arrays.equals(new String[] {"Alice" , "23"}, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] {"Bob"   , "34"}, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] {              }, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] {"Charly", "45"}, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] {"Dieter", "-1"}, iterator.next(container).getData()));
    }
    
    // helper ----------------------------------------------------------------------------------------------------------

    private static FixedWidthLineIterator createIterator(boolean ignoreEmptyLines) {
        PadFormat[] formats = new PadFormat[] {
                new PadFormat("", 6, Alignment.LEFT, ' '),
                new PadFormat("", 3, Alignment.RIGHT, '0'),
        };
        StringReader reader = new StringReader(
                "Alice 023" + SEP +
                "Bob   034" + SEP +
                "" + SEP +
                "Charly045" + SEP +
                "Dieter-01"
        );
        FixedWidthLineIterator iterator = new FixedWidthLineIterator(reader, formats, ignoreEmptyLines, null);
        return iterator;
    }

}
