package com.tomgs.es.gateway.module;

import com.google.inject.AbstractModule;
import com.tomgs.es.gateway.common.inject.multibindings.MapBinder;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.GenericAction;
import org.elasticsearch.action.admin.indices.close.CloseIndexAction;
import org.elasticsearch.action.admin.indices.close.TransportCloseIndexAction;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexAction;
import org.elasticsearch.action.admin.indices.delete.TransportDeleteIndexAction;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsAction;
import org.elasticsearch.action.admin.indices.exists.indices.TransportIndicesExistsAction;
import org.elasticsearch.action.admin.indices.exists.types.TransportTypesExistsAction;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsAction;
import org.elasticsearch.action.admin.indices.get.GetIndexAction;
import org.elasticsearch.action.admin.indices.get.TransportGetIndexAction;
import org.elasticsearch.action.admin.indices.open.OpenIndexAction;
import org.elasticsearch.action.admin.indices.open.TransportOpenIndexAction;
import org.elasticsearch.action.main.MainAction;
import org.elasticsearch.action.main.TransportMainAction;
import org.elasticsearch.action.support.TransportAction;
import org.elasticsearch.common.NamedRegistry;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.ActionPlugin.*;

import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 *  action module
 *
 * @author tomgs
 * @version 2019/5/6 1.0 
 */
public class ActionModule extends AbstractModule {

    private final Map<String, ActionHandler<?, ?>> actions;

    public ActionModule() {
        this.actions = setupActions();
    }

    private Map<String, ActionPlugin.ActionHandler<?, ?>> setupActions() {
        class ActionRegistry extends NamedRegistry<ActionHandler<?, ?>> {
            ActionRegistry() {
                super("action");
            }

            public void register(ActionHandler<?, ?> handler) {
                register(handler.getAction().name(), handler);
            }

            public <Request extends ActionRequest, Response extends ActionResponse> void register(
                    GenericAction<Request, Response> action, Class<? extends TransportAction<Request, Response>> transportAction,
                    Class<?>... supportTransportActions) {
                register(new ActionHandler<>(action, transportAction, supportTransportActions));
            }
        }
        ActionRegistry actions = new ActionRegistry();

        //这里添加action的处理类
        actions.register(MainAction.INSTANCE, TransportMainAction.class);

        actions.register(DeleteIndexAction.INSTANCE, TransportDeleteIndexAction.class);
        actions.register(GetIndexAction.INSTANCE, TransportGetIndexAction.class);
        actions.register(OpenIndexAction.INSTANCE, TransportOpenIndexAction.class);
        actions.register(CloseIndexAction.INSTANCE, TransportCloseIndexAction.class);
        actions.register(IndicesExistsAction.INSTANCE, TransportIndicesExistsAction.class);
        actions.register(TypesExistsAction.INSTANCE, TransportTypesExistsAction.class);

        return unmodifiableMap(actions.getRegistry());
    }

    public Map<String, ActionHandler<?, ?>> getActions() {
        return actions;
    }

    @Override
    protected void configure() {
        MapBinder<GenericAction, TransportAction> transportActionsBinder =
                MapBinder.newMapBinder(binder(), GenericAction.class, TransportAction.class);

        for (ActionHandler<?, ?> action : actions.values()) {
            // bind the action as eager singleton, so the map binder one will reuse it
            bind(action.getTransportAction()).asEagerSingleton();
            transportActionsBinder.addBinding(action.getAction()).to(action.getTransportAction()).asEagerSingleton();
            for (Class<?> supportAction : action.getSupportTransportActions()) {
                bind(supportAction).asEagerSingleton();
            }
        }
    }
}
