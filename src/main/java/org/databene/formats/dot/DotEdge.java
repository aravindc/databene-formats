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

/**
 * Represents an edge in a Dot graph.<br/><br/>
 * Created: 24.05.2014 08:07:52
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class DotEdge {
	
	private DotNode from;
	private DotNode to;
	private ArrowShape arrowHead;
	private String headLabel;
	private ArrowShape arrowTail;
	private String tailLabel;
	private EdgeStyle style;
	
	public DotEdge(DotNode from, DotNode to) {
		this.from = from;
		this.arrowHead = null;
		this.to = to;
		this.arrowTail = null;
		this.style = null;
	}
	
	public DotNode getFrom() {
		return from;
	}
	
	public DotNode getTo() {
		return to;
	}
	
	public ArrowShape getArrowHead() {
		return arrowHead;
	}
	
	public DotEdge withArrowHead(ArrowShape arrowHead) {
		this.arrowHead = arrowHead;
		return this;
	}

	public ArrowShape getArrowTail() {
		return arrowTail;
	}
	
	public DotEdge withArrowTail(ArrowShape arrowTail) {
		this.arrowTail = arrowTail;
		return this;
	}
	
	public String getHeadLabel() {
		return headLabel;
	}
	
	public DotEdge withHeadLabel(String headLabel) {
		this.headLabel = headLabel;
		return this;
	}
	
	public String getTailLabel() {
		return tailLabel;
	}
	
	public DotEdge withTailLabel(String tailLabel) {
		this.tailLabel = tailLabel;
		return this;
	}
	
	public EdgeStyle getStyle() {
		return style;
	}
	
	public DotEdge withStyle(EdgeStyle style) {
		this.style = style;
		return this;
	}
	
}
