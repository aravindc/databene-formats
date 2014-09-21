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
 * Represents a Dot graph.
 * @see http://www.graphviz.org/content/dot-language
 * Created: 24.05.2014 18:11:17
 * @since 0.8.2
 * @author Volker Bergmann
 */

public interface DotGraphModel {
	
	/** @return the name of the graph */
	String getName();
	
	/** Tells if the graph is directed */
	boolean isDirected();
	
	/** @return the graph's rankdir attribute */
	RankDir getRankDir();
	
	/** @return the shape of all graph nodes.
	 *  @see    http://www.graphviz.org/doc/info/shapes.html */
	NodeShape getNodeShape();
	
	Integer getNodeFontSize();
	
	/** @return the arrow shape of all edge heads of the graph
	 *  @see    http://www.graphviz.org/content/arrow-shapes */
	ArrowShape getEdgeArrowHead();
	
	/** @return the arrow shape of all edge tails of the graph
	 *  @see    http://www.graphviz.org/content/arrow-shapes */
	ArrowShape getEdgeArrowTail();
	
	String getNodeFillColor();
	
	NodeStyle getNodeStyle();
	
	EdgeStyle getEdgeStyle();
	
	
	
	/** @return the number of nodes in the graph */
	int getNodeCount();
	
	/** @return the graph node at the given index */
	Object getNode(int index);
	
	String getNodeId(Object node);
	
	boolean isNodeVertical(Object node);
	
	String getNodeLabel(Object node);
	
	Collection<?> getNodeSegments(Object node);
	
	String getNodeFillColor(Object node);
	
	NodeStyle getNodeStyle(Object node);
	
	
	
	int getEdgeCountOfNode(Object sourceNode);
	
	Object getEdgeOfNode(Object sourceNode, int index);
	
	Object getTargetNodeOfEdge(Object sourceNode, Object edge);
	
	ArrowShape getEdgeArrowHead(Object edge);
	
	String getEdgeHeadLabel(Object edge);
	
	ArrowShape getEdgeArrowTail(Object edge);
	
	String getEdgeTailLabel(Object edge);
	
	EdgeStyle getEdgeStyle(Object edge);
	
}
