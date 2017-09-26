"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require('@angular/core');
var forms_1 = require('@angular/forms');
var CP_SELECT_VALUE_ACCESSOR = {
    provide: forms_1.NG_VALUE_ACCESSOR,
    useExisting: core_1.forwardRef(function () { return SelectComponent; }),
    multi: true,
};
var SelectComponent = (function () {
    function SelectComponent(elRef) {
        this.elRef = elRef;
        this.tabIndex = 0;
        this.multiple = true;
        this.options = [];
        this.searchQuery = '';
        this.identifyFn = function (a, b) { return a === b; };
        this.onChange = function (_) { };
        this.onTouched = function () { };
    }
    SelectComponent.prototype.writeValue = function (obj) {
        var _this = this;
        this.value = obj;
        this.options.forEach(function (e) { return e.selected = false; });
        if (obj instanceof Array) {
            obj.forEach(function (e) {
                _this.options.filter(function (o) { return _this.identifyFn(o.value, e); }).forEach(function (o) { return o.selected = true; });
            });
        }
        else {
            this.options.filter(function (o) { return _this.identifyFn(o.value, obj); }).forEach(function (o) {
                o.selected = true;
                _this.selectedItem = o;
            });
        }
    };
    SelectComponent.prototype.registerOnChange = function (fn) {
        this.onChange = fn;
    };
    SelectComponent.prototype.registerOnTouched = function (fn) {
        this.onTouched = fn;
    };
    SelectComponent.prototype.onFocus = function (event) {
        this.showOptions();
        this.focused = true;
    };
    SelectComponent.prototype.onBlur = function (event) {
        // this.optionsVisible = false;
    };
    SelectComponent.prototype.onClick = function (event) {
        if (!this.elRef.nativeElement.contains(event.target)) {
            this.hideOptions();
            this.focused = false;
        }
    };
    SelectComponent.prototype.getPlaceholder = function () {
        if (this.optionsVisible) {
            if (!this.searchQuery || this.searchQuery === '') {
                return this.getViewText();
            }
            return '';
        }
        return this.placeholder || '';
    };
    SelectComponent.prototype.getSelectText = function () {
        if (this.optionsVisible) {
            return this.searchQuery || '';
        }
        return this.getViewText();
    };
    SelectComponent.prototype.getViewText = function () {
        if (this.multiple) {
            return this.getSelectedOptions().map(function (o) { return o.text; }).join(', ');
        }
        return this.selectedItem ? this.selectedItem.text : '';
    };
    SelectComponent.prototype.onSearchQueryChanged = function (query) {
        this.options.forEach(function (o) { return o.hidden = !o.text.includes(query); });
        this.searchQuery = query;
    };
    SelectComponent.prototype.onArrowClick = function () {
        this.toggleOptions();
    };
    SelectComponent.prototype.showOptions = function () {
        if (this.searchQuery !== '') {
            this.onSearchQueryChanged('');
            this.searchQuery = '';
        }
        // setTimeout(() => this.viewInput.nativeElement.focus(), 0);
        this.optionsVisible = true;
        this.onTouched();
    };
    SelectComponent.prototype.hideOptions = function () {
        this.optionsVisible = false;
        if (this.focused) {
        }
    };
    SelectComponent.prototype.toggleOptions = function () {
        this.optionsVisible ? this.hideOptions() : this.showOptions();
    };
    SelectComponent.prototype.addOption = function (option) {
        this.options.push(option);
        this.writeValue(this.value);
    };
    SelectComponent.prototype.removeOption = function (option) {
        this.options = this.options.filter(function (o) { return o.value !== option.value; });
        this.writeValue(this.value);
    };
    SelectComponent.prototype.onOptionClick = function (option) {
        var _this = this;
        if (this.multiple) {
            option.selected = !option.selected;
            this.onChange(this.getSelectedValues());
            return;
        }
        option.selected = true;
        this.options
            .filter(function (o) { return !_this.identifyFn(o.value, option.value); })
            .forEach(function (o) { return o.selected = false; });
        this.selectedItem = option;
        this.onChange(option.value);
        this.hideOptions();
    };
    SelectComponent.prototype.getSelectedOptions = function () {
        return this.options.filter(function (o) { return o.selected; });
    };
    SelectComponent.prototype.getSelectedValues = function () {
        return this.getSelectedOptions().map(function (o) { return o.value; });
    };
    __decorate([
        core_1.Input(), 
        __metadata('design:type', String)
    ], SelectComponent.prototype, "id", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', String)
    ], SelectComponent.prototype, "placeholder", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', Boolean)
    ], SelectComponent.prototype, "disabled", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', String)
    ], SelectComponent.prototype, "name", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', Number)
    ], SelectComponent.prototype, "tabIndex", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', String)
    ], SelectComponent.prototype, "ariaLabel", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', String)
    ], SelectComponent.prototype, "ariaLabelledby", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', Boolean)
    ], SelectComponent.prototype, "multiple", void 0);
    __decorate([
        core_1.HostBinding('class.focus'), 
        __metadata('design:type', Boolean)
    ], SelectComponent.prototype, "focused", void 0);
    __decorate([
        core_1.HostBinding('class.open'), 
        __metadata('design:type', Boolean)
    ], SelectComponent.prototype, "optionsVisible", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', Function)
    ], SelectComponent.prototype, "identifyFn", void 0);
    SelectComponent = __decorate([
        core_1.Component({
            selector: 'br-select',
            providers: [CP_SELECT_VALUE_ACCESSOR],
            host: {
                '(document:click)': 'onClick($event)',
            },
            template: "\n        <div class=\"cp-select\" [class.focus]=\"focused\">\n            <div class=\"cp-select-input-group\">\n                <input type=\"text\" class=\"cp-select-input\"\n                    autocomplete=\"off\"\n                    [readonly]=\"!optionsVisible\"\n                    [id]=\"id\"\n                    [placeholder]=\"getPlaceholder()\"\n                    [disabled]=\"disabled\"\n                    [tabIndex]=\"tabIndex\"\n                    [attr.name]=\"name\"\n                    [attr.aria-label]=\"ariaLabel\"\n                    [attr.aria-labelledby]=\"ariaLabelledby\"\n                    [ngModel]=\"getSelectText()\"\n                    (ngModelChange)=\"onSearchQueryChanged($event)\"\n                    (focus)=\"onFocus($event)\"\n                    (blur)=\"onBlur($event)\"\n                    #viewInput>\n                <div class=\"cp-select-arrow\" (click)=\"onArrowClick()\">\n                    <svg:svg viewBox=\"0 0 10 10\" preserveAspectRatio=\"xMidYMid\" width=\"10\" height=\"10\">\n                        <g class=\"cp-select-arrow\" [@arrow]=\"optionsVisible ? 'up' : 'down'\">\n                            <path d=\"m 1 3.5 l 8 0 l -4 4 z\" fill=\"#000000\"/>\n                        </g>\n                    </svg:svg>\n                </div>\n            </div>\n            <div class=\"cp-select-options-container\"\n                [@options]=\"optionsVisible ? 'visible' : 'hidden'\">\n                <div class=\"cp-select-options\" [style.maxHeight.px]=\"200\">\n                    <ng-content select=\"br-select-option\"></ng-content>\n                </div>\n            </div>\n        </div>\n    ",
            styles: ["\n        :host {\n            display: inline-block;\n            /*width: 100%;*/\n            position: relative;\n        }\n        .cp-select.focus > .cp-select-input-group {\n            box-shadow: 0 0 2px rgba(0,0,0,0.5);\n        }\n        .cp-select.focus > .cp-select-options-container {\n            box-shadow: 0 0 2px rgba(0,0,0,0.5);\n        }\n        .cp-select-options-container {\n            overflow: hidden;\n            position: absolute;\n            width: 100%;\n            background-color: white;\n            z-index: 1000;\n        }\n        .cp-select-input-group {\n            display: flex;\n        }\n        .cp-select-input {\n            width: 100%;\n            padding: 0.5rem;\n            flex: 1;\n            flex-basis: initial;\n            border: 1px solid black;\n            border-right: 0;\n        }\n        .cp-select-input:focus {\n            outline: none;\n        }\n        .cp-select-input:active {\n            outline: none;\n        }\n        .cp-select-arrow {\n            flex: 0;\n            border: 1px solid black;\n            border-left: 0;\n            transform-origin: 50% 50%;\n            cursor: pointer;\n            display: inline-flex;\n            align-items: center;\n            padding: 0 5px;\n        }\n        .cp-select-options {\n            overflow-y: auto;\n            display: flex;\n            flex-direction: column;\n            border-left: 1px solid black;\n            border-right: 1px solid black;\n        }\n    "],
            animations: [
                core_1.trigger('arrow', [
                    core_1.state('down', core_1.style({ transform: 'rotate(0deg)' })),
                    core_1.state('up', core_1.style({ transform: 'rotate(180deg)' })),
                    core_1.transition('down <=> up', core_1.animate('250ms ease')),
                ]),
                core_1.trigger('options', [
                    core_1.state('visible', core_1.style({ height: '*' })),
                    core_1.state('hidden', core_1.style({ height: 0 })),
                    core_1.transition('hidden => visible', [
                        core_1.style({ visibility: 'visible' }),
                        core_1.animate('250ms ease'),
                    ]),
                    core_1.transition('visible => hidden', [
                        core_1.animate('250ms ease', core_1.style({ height: 0 })),
                        core_1.style({ visibility: 'hidden' }),
                    ]),
                ]),
            ],
            changeDetection: core_1.ChangeDetectionStrategy.OnPush,
        }), 
        __metadata('design:paramtypes', [core_1.ElementRef])
    ], SelectComponent);
    return SelectComponent;
}());
exports.SelectComponent = SelectComponent;
//# sourceMappingURL=select.component.js.map