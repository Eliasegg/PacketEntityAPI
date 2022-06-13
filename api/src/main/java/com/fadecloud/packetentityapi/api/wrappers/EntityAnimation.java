package com.fadecloud.packetentityapi.api.wrappers;

import com.comphenix.protocol.PacketType;

import com.fadecloud.packetentityapi.api.CustomEntity;

public class EntityAnimation extends PacketWrapper {

    public EntityAnimation(CustomEntity entity) {
        super(entity);
    }

    public EntityAnimation queue(EntityAnimationType entityAnimationType) {
        return this.queue(entityAnimationType.id);
    }

    private EntityAnimation queue(int id) {
        super.newContainer(PacketType.Play.Server.ANIMATION).getIntegers()
                .write(0, super.entity.getId())
                .write(1, id);
        return this;
    }

    public enum EntityAnimationType {

        SWING_MAIN_ARM(0),
        TAKE_DAMAGE(1),
        LEAVE_BED(2),
        SWING_OFF_HAND(3),
        CRITICAL_EFFECT(4),
        MAGIC_CRITICAL_EFFECT(5);

        private final int id;

        EntityAnimationType(int id) {
            this.id = id;
        }

    }

}
