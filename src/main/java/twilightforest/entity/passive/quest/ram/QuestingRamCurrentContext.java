package twilightforest.entity.passive.quest.ram;

import twilightforest.beanification.Component;

@Component
public class QuestingRamCurrentContext {

	public static final QuestingRamCurrentContext SHARED = new QuestingRamCurrentContext();

	private QuestingRamContext context = QuestingRamContext.FALLBACK;

	public void setContext(QuestingRamContext context) {
		this.context = context;
	}

	public QuestingRamContext getContext() {
		return context;
	}

}
