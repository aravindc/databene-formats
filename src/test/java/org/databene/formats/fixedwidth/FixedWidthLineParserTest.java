/*
 * (c) Copyright 2010-2011 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

import static junit.framework.Assert.assertTrue;

import java.text.ParseException;
import java.util.Arrays;

import org.databene.commons.format.Alignment;
import org.databene.commons.format.PadFormat;
import org.junit.Test;

/**
 * Tests the {@link FixedWidthLineParser}.<br/><br/>
 * Created: 22.02.2010 08:19:12
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class FixedWidthLineParserTest {

	private static final PadFormat[] FORMATS = new PadFormat[] {
            new PadFormat("", 6, Alignment.LEFT, ' '),
            new PadFormat("", 3, Alignment.RIGHT, '0'),
    };
    
    private static final FixedWidthLineParser PARSER = new FixedWidthLineParser(FORMATS);

    @Test
    public void testProcessingEmptyLines() throws Exception {
    	
        check("Alice 023", "Alice", "23");
        check("Bob   034", "Bob", "34");
        check("Charly045","Charly", "45");
        check("Dieter-01", "Dieter", "-1");
    }

	private static void check(String line, String expectedName, String expectedAge) throws ParseException {
		assertTrue(Arrays.equals(new String[] { expectedName, expectedAge }, 
				PARSER.parse(line)));
    }

}
