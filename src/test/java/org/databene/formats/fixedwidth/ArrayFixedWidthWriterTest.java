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

package org.databene.formats.fixedwidth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.StringWriter;
import java.io.IOException;

import org.databene.commons.SystemInfo;
import org.databene.commons.format.Alignment;
import org.databene.formats.script.ConstantScript;

/**
 * Tests the {@link ArrayFixedWidthWriter}.<br/><br/>
 * Created: 16.06.2007 06:07:52
 * @since 0.1
 * @author Volker Bergmann
 */
public class ArrayFixedWidthWriterTest {

    private static final String SEP = SystemInfo.getLineSeparator();

    private static String RESULT =
            "header" + SEP + "1   23" + SEP + "14 156" + SEP + "footer";

    @Test
    public void test() throws IOException {
        StringWriter out = new StringWriter();
        ArrayFixedWidthWriter<Integer> writer = new ArrayFixedWidthWriter<Integer>(
                out, new ConstantScript("header" + SEP), new ConstantScript("footer"),
                new FixedWidthRowTypeDescriptor("default", new FixedWidthColumnDescriptor[] {
                        new FixedWidthColumnDescriptor(2, Alignment.LEFT),
                        new FixedWidthColumnDescriptor(3, Alignment.RIGHT),
                        new FixedWidthColumnDescriptor(1, Alignment.LEFT)
                })
        );
        writer.writeElement(new Integer[] {  1,  2, 3 });
        writer.writeElement(new Integer[] { 14, 15, 6 });
        writer.close();
        assertEquals(RESULT, out.toString());
    }

}
