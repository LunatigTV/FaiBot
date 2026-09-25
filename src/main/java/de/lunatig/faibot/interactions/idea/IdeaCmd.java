package de.lunatig.faibot.interactions.idea;

import de.lunatig.faibot.config.BotChannel;
import de.lunatig.faibot.interactions.ICommand;
import de.lunatig.faibot.interactions.components.IModalHandler;
import de.lunatig.faibot.localization.keys.Idea;
import de.lunatig.faibot.services.LocalizationService;
import de.lunatig.faibot.utils.ChannelProvider;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@RequiredArgsConstructor
public class IdeaCmd implements ICommand, IModalHandler {

    private final ChannelProvider channelProvider;
    private final IdeaComponents ideaComponents;
    private final LocalizationService i18n;

    @Override
    public String getComponentId() {
        return IdeaComponents.COMPONENT_ID;
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash("vorschlag", i18n.get(Idea.Command.DESCRIPTION)).setDefaultPermissions(DefaultMemberPermissions.ENABLED);
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        event.replyModal(ideaComponents.getIdeaModal()).queue();
    }

    @Override
    public void modalInteraction(ModalInteractionEvent event) {
        String subject = getRequiredValue(event, IdeaComponents.SUBJECT_FIELD);
        String body = getValueOrDefault(event, IdeaComponents.BODY_FIELD, "");

        MessageCreateData pollMessage = ideaComponents.getPollMessage(
                event.getUser().getAsMention(),
                subject, body
        );

        channelProvider.sendMessage(BotChannel.RECOMMENDATIONS, pollMessage);

        event.reply(i18n.get(Idea.Success.SUBMIT)).setEphemeral(true).queue();
    }
}