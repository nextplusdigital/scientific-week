(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('AnimalDialogController', AnimalDialogController);

    AnimalDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Animal', 'TipoAnimal'];

    function AnimalDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Animal, TipoAnimal) {
        var vm = this;

        vm.animal = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tipoanimals = TipoAnimal.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.animal.id !== null) {
                Animal.update(vm.animal, onSaveSuccess, onSaveError);
            } else {
                Animal.save(vm.animal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('romaninisistemasApp:animalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
