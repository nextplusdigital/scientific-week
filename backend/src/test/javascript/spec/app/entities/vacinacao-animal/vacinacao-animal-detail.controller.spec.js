'use strict';

describe('Controller Tests', function() {

    describe('VacinacaoAnimal Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVacinacaoAnimal, MockAnimal, MockVacinacao, MockRemedio;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVacinacaoAnimal = jasmine.createSpy('MockVacinacaoAnimal');
            MockAnimal = jasmine.createSpy('MockAnimal');
            MockVacinacao = jasmine.createSpy('MockVacinacao');
            MockRemedio = jasmine.createSpy('MockRemedio');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'VacinacaoAnimal': MockVacinacaoAnimal,
                'Animal': MockAnimal,
                'Vacinacao': MockVacinacao,
                'Remedio': MockRemedio
            };
            createController = function() {
                $injector.get('$controller')("VacinacaoAnimalDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'romaninisistemasApp:vacinacaoAnimalUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
