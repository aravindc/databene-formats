/*
 * (c) Copyright 2007-2014 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.text;

import java.io.IOException;

import org.databene.formats.text.DelocalizingConverter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the {@link DelocalizingConverter}.<br/><br/>
 * Created: 03.09.2006 19:29:56
 * @since 0.1
 * @author Volker Bergmann
 */
public class DelocalizingConverterTest {

	@Test
    public void testConversion() throws IOException {
        checkConversion("Abc", "Abc");
        checkConversion("ÄÖÜäöüß", "AeOeUeaeoeuess");
        checkConversion("áàâa", "aaaa");
        checkConversion("éèêe", "eeee");
        checkConversion("íìîi", "iiii");
        checkConversion("óòôo", "oooo");
        checkConversion("úùûu", "uuuu");
    }

    private static void checkConversion(String source, String expectedResult) throws IOException {
        DelocalizingConverter converter = new DelocalizingConverter();
        String result = converter.convert(source);
        assertEquals("Delocalization failed. ", expectedResult, result);
    }

}
