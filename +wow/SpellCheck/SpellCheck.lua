local function getSpells()
	local spells = {};
	for i = 1, GetNumSpellTabs() do
		local name, texture, offset, numSpells = GetSpellTabInfo(i);
		if not name then break; end
		for s = offset + 1, offset + numSpells do
			local spell, rank = GetSpellName(s, BOOKTYPE_SPELL);
			spells[spell] = s;
		end
	end
	return spells;
end

local function isBuffedBy(spell, target)
	local i = 1;
	local buff = UnitBuff(target, i);
	while buff do
		if buff == spell then return true; end
		i = i + 1;
		buff = UnitBuff(target, i);
	end
	return false;
end

local function isUsable(spell, id, target)
	if IsAttackSpell(spell) then return false; end
	if not IsUsableSpell(spell) then return false; end
	if IsSpellInRange(spell, target) == 1 then else return false; end
	local start, duration, enabled = GetSpellCooldown(spell);
	if duration > 0 then return false; end
	if isBuffedBy(spell, target) then return false; end
	return true;
end

function SpellCheck_ShowSpells()
	local spells = {};
	for k,v in pairs(getSpells()) do
		if isUsable(k, v, 'target') then
			spells[#spells + 1] = k;
		end
	end
        for i = 1, 32 do
    		local curr_spell = getglobal("SpellCheck_Spell" .. i);
    		if curr_spell == nil then
    			curr_spell = CreateFrame("Button", "SpellCheck_Spell" .. i, getglobal("SpellCheck"), "SpellCheck_SpellButton_Template");
    			curr_spell:SetID(i);
    			curr_spell:Show();
    			curr_spell:SetAttribute("type", "spell");
			curr_spell:SetAttribute("unit1", 'target');
    			if i == 1 then
				curr_spell:SetPoint("TOPLEFT", "SpellCheck", "TOPLEFT", 5, 4);
			else
				curr_spell:SetPoint("TOPLEFT", "SpellCheck_Spell"..(i-1), "TOPRIGHT");
			end
    		end
    		local curr_spell_icon = getglobal("SpellCheck_Spell" .. i .. "Icon");
    		if spells[i] then
            		curr_spell_icon:SetVertexColor(1,1,1);
            		local _, _, icon = GetSpellInfo(spells[i]);
			curr_spell_icon:SetTexture(icon);
			curr_spell:SetAttribute("spell1", spells[i]);
        	else
			curr_spell_icon:SetTexture(nil);
		end
        end
end