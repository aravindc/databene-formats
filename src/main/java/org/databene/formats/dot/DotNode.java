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

import java.util.ArrayList;
import java.util.List;

import org.databene.commons.Named;

/**
 * Represents a node in a Dot graph.<br/><br/>
 * Created: 24.05.2014 07:36:36
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class DotNode implements Named {
	
	private String name;
	private boolean vertical;
	private NodeStyle style;
	private String fillColor;
	private List<String> segments;
	private List<DotEdge> edges;

	public DotNode(String name) {
		this.name = name;
		this.vertical = true;
		this.style = null;
		this.fillColor = null;
		this.segments = new ArrayList<String>();
		this.edges = new ArrayList<DotEdge>();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public boolean isVertical() {
		return vertical;
	}
	
	public NodeStyle getStyle() {
		return style;
	}
	
	public DotNode withStyle(NodeStyle style) {
		this.style = style;
		return this;
	}
	
	public String getFillColor() {
		return fillColor;
	}
	
	public DotEdge newEdgeTo(DotNode target) {
		DotEdge edge = new DotEdge(this, target);
		edges.add(edge);
		return edge;
	}
	
	public List<DotEdge> getEdges() {
		return edges;
	}

	public DotNode withEdgeTo(DotNode target) {
		newEdgeTo(target);
		return this;
	}
	
	public List<String> getSegments() {
		return segments;
	}
	
	public DotNode withSegment(String... lines) {
		StringBuilder segment = new StringBuilder();
		for (String line : lines)
			segment.append(line).append("\\l");
		segments.add(segment.toString());
		return this;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
