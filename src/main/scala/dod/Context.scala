package dod

import dod.data.{AnimationRepository, AnimationSelectorRepository, FrameRepository, PhysicsRepository, PhysicsSelectorRepository, PositionRepository, SpriteRepository, StateRepository, TileRepository, TilesetRepository}
import dod.service.event.EventService
import dod.service.expression.ExpressionService
import dod.service.{GameObjectService, GameStageService, GraphicsService, KeyEventService, PhysicsService, PositionService, StateService}

class Context {

    private val tileRepository = new TileRepository
    private val tilesetRepository = new TilesetRepository
    val spriteRepository = new SpriteRepository(tileRepository, tilesetRepository)

    private val positionRepository = new PositionRepository

    private val stateRepository = new StateRepository

    private val physicsRepository = new PhysicsRepository
    private val physicsSelectorRepository = new PhysicsSelectorRepository(physicsRepository)

    private val frameRepository = new FrameRepository
    private val animationRepository = new AnimationRepository(frameRepository)
    private val animationSelectorRepository = new AnimationSelectorRepository(animationRepository)

    private val positionService = new PositionService(positionRepository)
    private val stateService = new StateService(stateRepository)
    private val physicsService = new PhysicsService(physicsSelectorRepository)
    private val graphicsService = new GraphicsService(animationSelectorRepository)
    val gameObjectService = new GameObjectService(positionService, stateService, physicsService, graphicsService)
    val gameStageService = new GameStageService(gameObjectService)

    val eventService = new EventService
    val keyEventService = new KeyEventService

}
