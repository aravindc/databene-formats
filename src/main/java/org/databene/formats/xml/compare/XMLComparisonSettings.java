/*
 * (c) Copyright 2015 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.xml.compare;

import org.databene.commons.SystemInfo;
import org.databene.formats.compare.ComparisonSettings;
import org.w3c.dom.Element;

/**
 * TODO Document class.<br/><br/>
 * Created: 19.11.2015 15:11:03
 * @since TODO version
 * @author Volker Bergmann
 */

public class XMLComparisonSettings extends ComparisonSettings<Element> {

    private boolean encodingRelevant;

    private boolean namespaceRelevant;

    /** Indicates if white space is relevant. */
    private boolean whitespaceRelevant;

    private String expectedLineSeparator;

    private boolean cdataRelevant;

    private boolean processingInstructionRelevant;

    private boolean includeRelevant;

	public XMLComparisonSettings() {
		this(new NameBasedXMLComparisonModel());
	}

	public XMLComparisonSettings(XMLComparisonModel model) {
		super(model);
		encodingRelevant = false;
        namespaceRelevant = false;
        whitespaceRelevant = false;
        expectedLineSeparator = SystemInfo.getLineSeparator();
        cdataRelevant = true;
        processingInstructionRelevant = false;
        includeRelevant = false;
	}

	@Override
	public XMLComparisonModel getModel() {
		return (XMLComparisonModel) super.getModel();
	}
	
    public boolean isEncodingRelevant() {
		return encodingRelevant;
	}

	public void setEncodingRelevant(boolean encodingRelevant) {
		this.encodingRelevant = encodingRelevant;
	}

	/** Tells if namespace is relevant
     * @return true if namespace is relevant, otherwise false */
    public boolean isNamespaceRelevant() {
        return namespaceRelevant;
    }

    /** Sets if namespace is relevant
     * @param namespaceRelevant true if namespace shall be relevant, otherwise false */
    public void setNamespaceRelevant(boolean namespaceRelevant) {
        this.namespaceRelevant = namespaceRelevant;
    }

    /** Tells if whitespace is relevant
     * @return true if whitespace is relevant, otherwise false */
    public boolean isWhitespaceRelevant() {
        return whitespaceRelevant;
    }

    /** Sets if whitespace is relevant
     * @param whitespaceRelevant true if whitespace shall be relevant, otherwise false */
    public void setWhitespaceRelevant(boolean whitespaceRelevant) {
        this.whitespaceRelevant = whitespaceRelevant;
    }

    /** Provides the expected line separator or null if any line separator is accepted.
     * @return the expected line separator or null if any line separator is accepted. */
    public String getExpectedLineSeparator() {
        return expectedLineSeparator;
    }

    /** Sets the expected line separator.
     * @param expectedLineSeparator the expected line separator or null if any line separator is accepted. */
    public void setExpectedLineSeparator(String expectedLineSeparator) {
        this.expectedLineSeparator = expectedLineSeparator;
    }

    /** Tells if CDATA is relevant
     * @return true if CDATA is relevant, otherwise false */
    public boolean isCdataRelevant() {
        return cdataRelevant;
    }

    /** Sets if CDATA is relevant
     * @param cdataRelevant true if CDATA shall be relevant, otherwise false */
    public void setCdataRelevant(boolean cdataRelevant) {
        this.cdataRelevant = cdataRelevant;
    }

    /** Tells if processing instructions are relevant
     * @return true if processing instructions are relevant, otherwise false */
    public boolean isProcessingInstructionRelevant() {
        return processingInstructionRelevant;
    }

    /** Sets if processing instructions are relevant
     * @param processingInstructionRelevant true if processing instructions shall be relevant, otherwise false */
    public void setProcessingInstructionRelevant(boolean processingInstructionRelevant) {
        this.processingInstructionRelevant = processingInstructionRelevant;
    }

    /** Tells if includes are relevant
     * @return true if includes are relevant, otherwise false */
    public boolean isIncludeRelevant() {
        return includeRelevant;
    }

    /** Sets if includes are relevant
     * @param includeRelevant true if includes shall be relevant, otherwise false */
    public void setIncludeRelevant(boolean includeRelevant) {
        this.includeRelevant = includeRelevant;
    }

}
