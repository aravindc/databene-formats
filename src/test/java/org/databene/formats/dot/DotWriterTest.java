/*
 * (c) Copyright 2014 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.dot;

import static org.junit.Assert.*;

import java.io.FileOutputStream;

import org.databene.commons.Encodings;
import org.databene.commons.IOUtil;
import org.databene.formats.dot.ArrowShape;
import org.databene.formats.dot.DefaultDotGraphModel;
import org.databene.formats.dot.DotGraph;
import org.databene.formats.dot.DotNode;
import org.databene.formats.dot.DotWriter;
import org.databene.formats.dot.EdgeStyle;
import org.databene.formats.dot.NodeShape;
import org.databene.formats.dot.NodeStyle;
import org.databene.formats.dot.RankDir;
import org.junit.Test;

/**
 * Tests the {@link DotWriter}.<br/><br/>
 * Created: 24.05.2014 08:13:11
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class DotWriterTest {
	
	@Test
	public void test() throws Exception {
		DotGraph graph = DotGraph.newDirectedGraph("geo").withRankDir(RankDir.BT)
				.withNodeShape(NodeShape.record).withNodeFontSize(12).withNodeStyle(NodeStyle.filled).withNodeFillColor("yellow")
				.withArrowHead(ArrowShape.open);
		DotNode country = graph.newNode("country").withSegment("CNTY").withSegment("DE", "FR", "IT");
		DotNode state = graph.newNode("state").withEdgeTo(country).withSegment("STATE");
		DotNode city = graph.newNode("city").withEdgeTo(state);
		city.newEdgeTo(country).withStyle(EdgeStyle.dotted);
		DefaultDotGraphModel model = new DefaultDotGraphModel(graph);
		DotWriter.persist(model, new FileOutputStream("target/geo.dot"), Encodings.UTF_8);
		String actual = IOUtil.getContentOfURI("target/geo.dot");
		String expected = IOUtil.getContentOfURI("org/databene/formats/dot/geo.dot");
		assertEquals(expected, actual);
	}
	
}
