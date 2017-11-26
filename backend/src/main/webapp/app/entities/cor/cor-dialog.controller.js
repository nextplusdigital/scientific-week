(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('CorDialogController', CorDialogController);

    CorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cor'];

    function CorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cor) {
        var vm = this;

        vm.cor = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cor.id !== null) {
                Cor.update(vm.cor, onSaveSuccess, onSaveError);
            } else {
                Cor.save(vm.cor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('romaninisistemasApp:corUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
