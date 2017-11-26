(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('TipoAnimalDetailController', TipoAnimalDetailController);

    TipoAnimalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TipoAnimal'];

    function TipoAnimalDetailController($scope, $rootScope, $stateParams, previousState, entity, TipoAnimal) {
        var vm = this;

        vm.tipoAnimal = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('romaninisistemasApp:tipoAnimalUpdate', function(event, result) {
            vm.tipoAnimal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
