(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('CorDetailController', CorDetailController);

    CorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cor'];

    function CorDetailController($scope, $rootScope, $stateParams, previousState, entity, Cor) {
        var vm = this;

        vm.cor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('romaninisistemasApp:corUpdate', function(event, result) {
            vm.cor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
