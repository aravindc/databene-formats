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

import java.util.Collection;

/**
 * Default implementation of a {@link DotGraphModel}, making use of {@link DotGraph}, 
 * {@link DotNode}s and {@link DotEdge}s.<br/><br/>
 * Created: 24.05.2014 18:17:07
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class DefaultDotGraphModel implements DotGraphModel {
	
	private DotGraph graph;
	
	public DefaultDotGraphModel(DotGraph graph) {
		this.graph = graph;
	}

	@Override
	public String getName() {
		return graph.getName();
	}

	@Override
	public boolean isDirected() {
		return graph.isDirected();
	}

	@Override
	public RankDir getRankDir() {
		return graph.getRankDir();
	}

	@Override
	public NodeShape getNodeShape() {
		return graph.getNodeShape();
	}

	@Override
	public Integer getNodeFontSize() {
		return graph.getNodeFontSize();
	}
	
	@Override
	public NodeStyle getNodeStyle() {
		return graph.getNodeStyle();
	}
	
	@Override
	public String getNodeFillColor() {
		return graph.getNodeFillColor();
	}

	@Override
	public ArrowShape getEdgeArrowHead() {
		return graph.getEdgeArrowHead();
	}

	@Override
	public ArrowShape getEdgeArrowTail() {
		return graph.getEdgeArrowTail();
	}
	
	@Override
	public EdgeStyle getEdgeStyle() {
		return graph.getEdgeStyle();
	}
	
	

	@Override
	public int getNodeCount() {
		return graph.getNodes().size();
	}

	@Override
	public Object getNode(int index) {
		return graph.getNodes().get(index);
	}

	@Override
	public String getNodeId(Object node) {
		return ((DotNode) node).getName();
	}
	
	@Override
	public boolean isNodeVertical(Object node) {
		return ((DotNode) node).isVertical();
	}

	@Override
	public String getNodeLabel(Object node) {
		return DotUtil.segmentsToLabel(getNodeSegments(node), isNodeVertical(node));
	}

	@Override
	public Collection<?> getNodeSegments(Object node) {
		return ((DotNode) node).getSegments();
	}

	@Override
	public String getNodeFillColor(Object node) {
		return ((DotNode) node).getFillColor();
	}

	@Override
	public NodeStyle getNodeStyle(Object node) {
		return ((DotNode) node).getStyle();
	}
	
	
	
	@Override
	public int getEdgeCountOfNode(Object node) {
		return ((DotNode) node).getEdges().size();
	}

	@Override
	public Object getEdgeOfNode(Object node, int index) {
		return ((DotNode) node).getEdges().get(index);
	}

	@Override
	public Object getTargetNodeOfEdge(Object sourceNode, Object edge) {
		return ((DotEdge) edge).getTo();
	}

	@Override
	public ArrowShape getEdgeArrowHead(Object edge) {
		return ((DotEdge) edge).getArrowHead();
	}

	@Override
	public String getEdgeHeadLabel(Object edge) {
		return ((DotEdge) edge).getHeadLabel();
	}

	@Override
	public ArrowShape getEdgeArrowTail(Object edge) {
		return ((DotEdge) edge).getArrowTail();
	}

	@Override
	public String getEdgeTailLabel(Object edge) {
		return ((DotEdge) edge).getTailLabel();
	}

	@Override
	public EdgeStyle getEdgeStyle(Object edge) {
		return ((DotEdge) edge).getStyle();
	}
	
}
