(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('AnimalDetailController', AnimalDetailController);

    AnimalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Animal', 'TipoAnimal'];

    function AnimalDetailController($scope, $rootScope, $stateParams, previousState, entity, Animal, TipoAnimal) {
        var vm = this;

        vm.animal = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('romaninisistemasApp:animalUpdate', function(event, result) {
            vm.animal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
