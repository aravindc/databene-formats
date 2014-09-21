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
 * Abstract implementation of the {@link DotGraphModel}.<br/><br/>
 * Created: 25.05.2014 06:29:29
 * @since 0.8.2
 * @author Volker Bergmann
 */

public abstract class AbstractDotGraphModel implements DotGraphModel {
	
	private String name;
	private boolean directed;
	private RankDir rankDir;
	private NodeShape nodeShape;
	private Integer nodeFontSize;
	private NodeStyle nodeStyle;
	private String nodeFillColor;
	private ArrowShape arrowHead;
	private ArrowShape arrowTail;
	private EdgeStyle edgeStyle;
	
	
	public AbstractDotGraphModel() {
		this.name = null;
		this.directed = true;
		this.rankDir = null;
		this.nodeShape = null;
		this.nodeFontSize = null;
		this.nodeStyle = null;
		this.nodeFillColor = null;
		this.arrowHead = null;
		this.arrowTail = null;
		this.edgeStyle = null;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public AbstractDotGraphModel setName(String name) {
		this.name = name;
		return this;
	}
	
	@Override
	public boolean isDirected() {
		return directed;
	}
	
	public AbstractDotGraphModel withDirected() {
		this.directed = true;
		return this;
	}
	
	public AbstractDotGraphModel withUndirected() {
		this.directed = false;
		return this;
	}
	
	@Override
	public RankDir getRankDir() {
		return rankDir;
	}
	
	public AbstractDotGraphModel withRankDir(RankDir rankDir) {
		this.rankDir = rankDir;
		return this;
	}
	
	@Override
	public NodeShape getNodeShape() {
		return nodeShape;
	}
	
	public AbstractDotGraphModel withNodeShape(NodeShape nodeShape) {
		this.nodeShape = nodeShape;
		return this;
	}
	
	@Override
	public Integer getNodeFontSize() {
		return nodeFontSize;
	}
	
	public AbstractDotGraphModel withNodeFontSize(int fontSize) {
		this.nodeFontSize = fontSize;
		return this;
	}
	
	@Override
	public NodeStyle getNodeStyle() {
		return nodeStyle;
	}
	
	public AbstractDotGraphModel withNodeStyle(NodeStyle nodeStyle) {
		this.nodeStyle = nodeStyle;
		return this;
	}
	
	@Override
	public String getNodeFillColor() {
		return nodeFillColor;
	}
	
	public AbstractDotGraphModel withNodeFillColor(String color) {
		this.nodeFillColor = color;
		return this;
	}
	
	
	
	@Override
	public ArrowShape getEdgeArrowHead() {
		return arrowHead;
	}
	
	public AbstractDotGraphModel withEdgeArrowHead(ArrowShape arrowHead) {
		this.arrowHead = arrowHead;
		return this;
	}
	
	@Override
	public ArrowShape getEdgeArrowTail() {
		return arrowTail;
	}
	
	public AbstractDotGraphModel withEdgeArrowTail(ArrowShape arrowTail) {
		this.arrowTail = arrowTail;
		return this;
	}
	
	@Override
	public EdgeStyle getEdgeStyle() {
		return edgeStyle;
	}
	
	public AbstractDotGraphModel withEdgeStyle(EdgeStyle edgeStyle) {
		this.edgeStyle = edgeStyle;
		return this;
	}
	
	
	
	@Override
	public boolean isNodeVertical(Object node) {
		return true;
	}
	
	@Override
	public String getNodeLabel(Object node) {
		return DotUtil.segmentsToLabel(getNodeSegments(node), isNodeVertical(node));
	}
	
	@Override
	public Collection<?> getNodeSegments(Object node) {
		return null;
	}
	
	@Override
	public NodeStyle getNodeStyle(Object node) {
		return null;
	}
	
	@Override
	public String getNodeFillColor(Object node) {
		return null;
	}
	
	
	
	@Override
	public ArrowShape getEdgeArrowHead(Object edge) {
		return null;
	}
	
	@Override
	public String getEdgeHeadLabel(Object edge) {
		return null;
	}
	
	@Override
	public ArrowShape getEdgeArrowTail(Object edge) {
		return null;
	}
	
	@Override
	public String getEdgeTailLabel(Object edge) {
		return null;
	}
	
	@Override
	public EdgeStyle getEdgeStyle(Object edge) {
		return null;
	}
	
}
