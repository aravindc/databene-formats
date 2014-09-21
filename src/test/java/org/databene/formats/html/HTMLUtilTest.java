/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.html;

import static org.junit.Assert.*;

import java.util.Map;

import org.databene.commons.CollectionUtil;
import org.databene.formats.html.util.HTMLUtil;
import org.junit.Test;

/**
 * Tests the {@link HTMLUtil} class.<br/><br/>
 * Created: 22.11.2010 08:09:08
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class HTMLUtilTest {
	
	@Test
	public void test() {
		@SuppressWarnings("unchecked")
		Map<String, String> expected = CollectionUtil.buildMap("loc", "60", "f", "test.info");
		Map<String, String> actual = HTMLUtil.parseCGIParameters("http://myhost.com/info.php?loc=60&f=test.info");
		assertEquals(expected, actual);
	}

}
