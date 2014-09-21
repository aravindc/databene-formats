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

import java.util.List;

import org.databene.commons.Named;
import org.databene.commons.collection.OrderedNameMap;

/**
 * Represents a Dot graph.<br/><br/>
 * Created: 24.05.2014 06:01:34
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class DotGraph implements Named {
	
	private String name;
	private final boolean directed;
	private RankDir rankDir;
	
	private NodeShape nodeShape;
	private Integer nodeFontSize;
	private NodeStyle nodeStyle;
	private String nodeFillColor;
	
	private EdgeStyle edgeStyle;
	private ArrowShape edgeArrowHead;
	private ArrowShape edgeArrowTail;
	private OrderedNameMap<DotNode> nodes;
	
	private DotGraph(String name, boolean directed) {
		this.name = name;
		this.directed = directed;
		this.rankDir = null;
		this.nodeShape = null;
		this.nodeFontSize = null;
		this.nodeStyle = null;
		this.nodeFillColor = null;
		this.edgeStyle = null;
		this.edgeArrowHead = null;
		this.edgeArrowTail = null;
		this.nodes = new OrderedNameMap<DotNode>();
	}
	
	public static DotGraph newDirectedGraph(String name) {
		return new DotGraph(name, true);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public boolean isDirected() {
		return directed;
	}
	
	public RankDir getRankDir() {
		return rankDir;
	}
	
	public DotGraph withRankDir(RankDir rankDir) {
		this.rankDir = rankDir;
		return this;
	}
	
	public NodeShape getNodeShape() {
		return nodeShape;
	}
	
	public DotGraph withNodeShape(NodeShape shape) {
		this.nodeShape = shape;
		return this;
	}
	
	public Integer getNodeFontSize() {
		return nodeFontSize;
	}
	
	public DotGraph withNodeFontSize(int points) {
		this.nodeFontSize = points;
		return this;
	}
	
	public NodeStyle getNodeStyle() {
		return this.nodeStyle;
	}
	
	public DotGraph withNodeStyle(NodeStyle style) {
		this.nodeStyle = style;
		return this;
	}
	
	public String getNodeFillColor() {
		return nodeFillColor;
	}
	
	public DotGraph withNodeFillColor(String fillColor) {
		this.nodeFillColor = fillColor;
		return this;
	}
	
	
	
	public EdgeStyle getEdgeStyle() {
		return edgeStyle;
	}
	
	public DotGraph withEdgeStyle(EdgeStyle edgeStyle) {
		this.edgeStyle = edgeStyle;
		return this;
	}
	
	public ArrowShape getEdgeArrowHead() {
		return edgeArrowHead;
	}
	
	public DotGraph withArrowHead(ArrowShape arrowHead) {
		this.edgeArrowHead = arrowHead;
		return this;
	}
	
	public ArrowShape getEdgeArrowTail() {
		return edgeArrowTail;
	}
	
	public DotGraph withEdgeArrowTail(ArrowShape arrowTail) {
		this.edgeArrowTail = arrowTail;
		return this;
	}
	
	public DotNode newNode(String name) {
		DotNode node = new DotNode(name);
		nodes.put(node.getName(), node);
		return node;
	}
	
	public List<DotNode> getNodes() {
		return nodes.values();
	}

}
