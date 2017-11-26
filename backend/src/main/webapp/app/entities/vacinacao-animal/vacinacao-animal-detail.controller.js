(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('VacinacaoAnimalDetailController', VacinacaoAnimalDetailController);

    VacinacaoAnimalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VacinacaoAnimal', 'Animal', 'Vacinacao', 'Remedio'];

    function VacinacaoAnimalDetailController($scope, $rootScope, $stateParams, previousState, entity, VacinacaoAnimal, Animal, Vacinacao, Remedio) {
        var vm = this;

        vm.vacinacaoAnimal = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('romaninisistemasApp:vacinacaoAnimalUpdate', function(event, result) {
            vm.vacinacaoAnimal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
