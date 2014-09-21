/*
 * (c) Copyright 2005-2014 by Volker Bergmann. All rights reserved.
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

import java.util.Locale;
import java.text.ParseException;
import java.text.FieldPosition;

import org.databene.formats.text.AbbreviatedNumberFormat;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the {@link AbbreviatedNumberFormat}.
 * Created: 16.05.2005 22:04:10
 * @since 0.1
 * @author Volker Bergmann
 */
public class AbbreviatedNumberFormatTest {
    private static final double DELTA = 0.0001;

    @Test
    public void testParse() throws Exception {
        checkParse("1", 1, Locale.US);
        checkParse("1 Tsd", 1000, Locale.US);
        checkParse("1. Tsd", 1000, Locale.US);
        checkParse("1.234 Tsd", 1234, Locale.US);
        checkParse("1,234.56 Mio", 1234560000, Locale.US);
        checkParse("1", 1, Locale.GERMANY);
        checkParse("1 Tsd", 1000, Locale.GERMANY);
        checkParse("1, Tsd", 1000, Locale.GERMANY);
        checkParse("1,234 Tsd", 1234, Locale.GERMANY);
        checkParse("1,234 Tsd", 1234, Locale.GERMANY);
        checkParse("1.234,56 Mio", 1234560000, Locale.GERMANY);
    }

    @Test
    public void testFormat() throws Exception {
        checkFormat(1, "1.00", Locale.US);
        checkFormat(1000, "1.00 Tsd", Locale.US);
        checkFormat(1234, "1.23 Tsd", Locale.US);
        checkFormatFixed(1234560000, "1,234.56 Mio", 1000000, Locale.US);
        checkFormat(1, "1,00", Locale.GERMANY);
        checkFormat(1000, "1,00 Tsd", Locale.GERMANY);
        checkFormat(1234, "1,23 Tsd", Locale.GERMANY);
        checkFormatFixed(1234560000, "1.234,56 Mio", 1000000, Locale.GERMANY);
    }

    @Test
    public void testFormatFixed() throws Exception {
        checkFormatFixed(1, "1.00", 1, Locale.US);
        checkFormatFixed(123, "0.12 Tsd", 1000, Locale.US);
        checkFormatFixed(1000, "1.00 Tsd", 1000, Locale.US);
        checkFormatFixed(1234, "1.23 Tsd", 1000, Locale.US);
        checkFormatFixed(1234560000, "1,234.56 Mio", 1000000, Locale.US);
        checkFormatFixed(1, "1,00", 1, Locale.GERMANY);
        checkFormatFixed(123, "0,12 Tsd", 1000, Locale.GERMANY);
        checkFormatFixed(1000, "1,00 Tsd", 1000, Locale.GERMANY);
        checkFormatFixed(1234, "1,23 Tsd", 1000, Locale.GERMANY);
        checkFormatFixed(1234560000, "1.234,56 Mio", 1000000, Locale.GERMANY);
    }

    private static void checkParse(String source, double target, Locale locale) throws ParseException {
        AbbreviatedNumberFormat format = new AbbreviatedNumberFormat(locale);
        assertEquals(target, format.parse(source).doubleValue(), DELTA);
    }

    private static void checkFormat(double source, String target, Locale locale) {
        AbbreviatedNumberFormat format = new AbbreviatedNumberFormat(locale);
        String result = format.format(source);
        assertEquals(target, result);
    }

    private static void checkFormatFixed(double source, String target, double defaultScale, Locale locale) {
        AbbreviatedNumberFormat format = new AbbreviatedNumberFormat(defaultScale, locale);
        String result = format.formatFixed(source, new StringBuffer(), new FieldPosition(0)).toString();
        assertEquals(target, result);
    }

}
