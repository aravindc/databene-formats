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

package org.databene.formats.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import org.databene.commons.DocumentWriter;
import org.databene.commons.SystemInfo;
import org.databene.formats.script.ConstantScript;
import org.databene.test.TP;

/**
 * Tests the BeanPropertiesFileWriter.<br/>
 * <br/>
 * Created: 16.06.2007 06:07:52
 * @since 0.1
 * @author Volker Bergmann
 */
public class BeanPropertiesFileWriterTest {

    private static final String SEP = SystemInfo.getLineSeparator();

    private static String UNPREFIXED_RESULT =
            "# header" + SEP + "class=org.databene.test.TP" + SEP + "name=Carl" + SEP + "age=48" + SEP + "# footer";

    private static String PREFIXED_RESULT =
            "# header" + SEP +
            "person1.class=org.databene.test.TP" + SEP + "person1.name=Carl" + SEP + "person1.age=48" + SEP +
            "person2.class=org.databene.test.TP" + SEP + "person2.name=Carl" + SEP + "person2.age=48" + SEP +
            "# footer";

    @Test
    public void testEscaping() throws IOException {
        StringWriter out = new StringWriter();
        DocumentWriter<TP> writer = new BeanPropertiesFileWriter<TP>(
                out, null, (String)null, null, new String[] { "class", "name", "age" }
        );
        TP person = new TP("Al\\f");
        writer.writeElement(person);
        writer.close();
        assertEquals("class=org.databene.test.TP" + SEP + "name=Al\\\\f" + SEP + "age=48" + SEP, out.toString());
    }

    @Test
    public void testUnprefixed() throws IOException {
        StringWriter out = new StringWriter();
        DocumentWriter<TP> writer = new BeanPropertiesFileWriter<TP>(
                out,
                null,
                new ConstantScript("# header" + SEP),
                new ConstantScript("# footer"),
                new String[] { "class", "name", "age" }
        );
        TP person = new TP();
        writer.writeElement(person);
        writer.close();
        assertEquals(UNPREFIXED_RESULT, out.toString());
    }

    @Test
    public void testPrefixed() throws IOException {
        StringWriter out = new StringWriter();
        DocumentWriter<TP> writer = new BeanPropertiesFileWriter<TP>(
                out,
                "person{0}.",
                new ConstantScript("# header" + SEP),
                new ConstantScript("# footer"),
                new String[] { "class", "name", "age" }
        );
        TP person = new TP();
        writer.writeElement(person);
        writer.writeElement(person);
        writer.close();
        assertEquals(PREFIXED_RESULT, out.toString());
    }

}
