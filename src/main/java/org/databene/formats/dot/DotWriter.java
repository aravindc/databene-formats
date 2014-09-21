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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.databene.commons.Encodings;
import org.databene.commons.IOUtil;
import org.databene.commons.StringUtil;
import org.databene.commons.collection.OrderedNameMap;

/**
 * Writes Dot files.<br/><br/>
 * Created: 24.05.2014 09:29:45
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class DotWriter {

	public static void persist(DotGraphModel model, OutputStream os) {
		persist(model, os, Encodings.UTF_8);
	}
		
	public static void persist(DotGraphModel model, OutputStream os, String encoding) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new OutputStreamWriter(os, encoding));
			// header
			out.print(model.isDirected() ? "digraph" : "graph");
			if (!StringUtil.isEmpty(model.getName()))
				out.print(" " + model.getName());
			out.println(" {");
			
			// graph attributes
			new AttributesWriter("graph")
				.add("charset", encoding)
				.add("rankdir", model.getRankDir())
				.write(out);
			
			// node attributes
			new AttributesWriter("node")
				.add("shape", model.getNodeShape())
				.add("fontsize", model.getNodeFontSize())
				.add("style", model.getNodeStyle())
				.add("fillcolor", DotUtil.normalizeColor(model.getNodeFillColor()))
				.write(out);
			
			// edge attributes
			new AttributesWriter("edge")
				.add("arrowhead", model.getEdgeArrowHead())
				.add("arrowtail", model.getEdgeArrowTail())
				.add("style", model.getEdgeStyle())
				.write(out);
			
			// persist nodes
			for (int i = 0; i < model.getNodeCount(); i++) {
				Object node = model.getNode(i);
				writeNode(node, model, out);
			}
			
			// persist edges
			for (int i = 0; i < model.getNodeCount(); i++) {
				Object node = model.getNode(i);
				writeEdges(node, model, out);
			}
			
			// end
			out.println("}");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException("Not a supported encoding: " + encoding, e);
		} finally {
			IOUtil.close(out);
		}
	}

	private static void writeNode(Object node, DotGraphModel model, PrintWriter out) {
		out.print("\t" + model.getNodeId(node));
		String label = model.getNodeLabel(node);
		if (label != null)
			label = '"' + label + '"';
		new AttributesWriter()
			.add("label", label)
			.add("fillcolor", DotUtil.normalizeColor(model.getNodeFillColor(node)))
			.write(out);
	}
	
	private static void writeEdges(Object node, DotGraphModel model, PrintWriter out) {
		for (int i = 0; i < model.getEdgeCountOfNode(node); i++) {
			Object edge = model.getEdgeOfNode(node, i);
			writeEdge(node, edge, model, out);
		}
	}

	private static void writeEdge(Object sourceNode, Object edge, DotGraphModel model, PrintWriter out) {
		Object target = model.getTargetNodeOfEdge(sourceNode, edge);
		out.print("\t" + model.getNodeId(sourceNode) + " " + (model.isDirected() ? "->" : "--") + " " + model.getNodeId(target));
		new AttributesWriter()
			.add("arrowhead", model.getEdgeArrowHead(edge))
			.add("headlabel", model.getEdgeHeadLabel(edge))
			.add("arrowtail", model.getEdgeArrowTail(edge))
			.add("taillabel", model.getEdgeTailLabel(edge))
			.add("style", model.getEdgeStyle(edge))
			.write(out);
	}
	
	static class AttributesWriter {
		String label;
		OrderedNameMap<Object> map;
		
		public AttributesWriter() {
			this(null);
		}

		public AttributesWriter(String label) {
			this.label = label;
			this.map = new OrderedNameMap<Object>();
		}

		public AttributesWriter add(String name, Object value) {
			if (value != null)
				map.put(name, value);
			return this;
		}
		
		public void write(PrintWriter out) {
			if (!map.isEmpty()) {
				if (label != null)
					out.print("\t" + label);
				out.print(" [");
				boolean first = true;
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if (!first)
						out.print(", ");
					out.print(entry.getKey() + "=" + entry.getValue());
					first = false;
				}
				out.print("]");
			}
			if (label == null || !map.isEmpty()) {
				out.println(";");
			}
		}
	}
	
}
