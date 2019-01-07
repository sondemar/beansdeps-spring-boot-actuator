package com.sondecki.beansdeps;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.beans.BeansEndpoint;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

@RestControllerEndpoint(id = "beans-deps")
@RequiredArgsConstructor
public class BeansDepsEndpoint {

    private final BeansEndpoint beansEndpoint;

    @GetMapping
    public void beans(HttpServletResponse response) throws IOException {
        response.setContentType("image/svg+xml");
        List<MutableGraph> contextsGraphs = new ArrayList<>();

        Map<String, BeansEndpoint.ContextBeans> contexts = beansEndpoint.beans().getContexts();
        for (Map.Entry<String, BeansEndpoint.ContextBeans> ctxEntry : contexts.entrySet()) {
            MutableGraph ctxGraph = mutGraph(ctxEntry.getKey()).setDirected(true);
            for (Map.Entry<String, BeansEndpoint.BeanDescriptor> beanEntry : ctxEntry.getValue().getBeans().entrySet()) {
                MutableNode beanNode = mutNode(beanEntry.getKey());
                for (String beanName: beanEntry.getValue().getDependencies()) {
                    beanNode.addLink(beanName);
                }
                ctxGraph.add(beanNode);
            }
            contextsGraphs.add(ctxGraph);
        }

        Graphviz.fromGraph(mutGraph()
                .setDirected(true)
                .add(contextsGraphs.toArray(new MutableGraph[0])))
                .render(Format.SVG)
                .toOutputStream(response.getOutputStream());

        response.flushBuffer();
    }
}
