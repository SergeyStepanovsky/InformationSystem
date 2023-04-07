package project.SpringDemoBot.LaTexConvert;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import com.formdev.flatlaf.util.StringUtils;

import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleOutput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;

@Path("/mathml")
public class MathMLService {

    @GET
    @Path("/{formula}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMathMLFormula(@PathParam("formula") String formula) throws Exception {
//         SnuggleEngine engine = new SnuggleEngine();
//         SnuggleSession session = engine.createSession();
//         SnuggleInput input = new SnuggleInput(formula);
//         SnuggleOutput output = session.parseInput(input);

        Document document = output.createMathMLDocument();
        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS implementation = (DOMImplementationLS) registry.getDOMImplementation("LS");
        LSSerializer serializer = implementation.createLSSerializer();

        NodeList nodeList = document.getElementsByTagName("math");
        Node mathNode = nodeList.item(0);

        return StringUtils.remove(serializer.writeToString(mathNode), "<?xml version=\"1.0\" encoding=\"UTF-16\"?>");
    }
}
